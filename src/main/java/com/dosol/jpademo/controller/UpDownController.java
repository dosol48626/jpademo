package com.dosol.jpademo.controller;

import com.dosol.jpademo.dto.BoardDTO;
import com.dosol.jpademo.dto.upload.UploadFileDTO;
import com.dosol.jpademo.dto.upload.UploadResultDTO;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@Log4j2
@RequestMapping("/upload")
public class UpDownController {

    @Value("${com.dosol.jpademo.upload.path}")
    private String uploadPath;

    @GetMapping("/uploadForm")
    public void uploadForm() {
    }

    @PostMapping("/uploadPro")
    public void uploadPro(UploadFileDTO uploadFileDTO, BoardDTO boardDTO, Model model) {
        //log.info(uploadFileDTO);
        log.info(boardDTO);
        if (uploadFileDTO.getFiles() != null) {
            final List<UploadResultDTO> list = new ArrayList<>();
            uploadFileDTO.getFiles().forEach(multipartFile -> {
                String originalName = multipartFile.getOriginalFilename();
                log.info(originalName);

                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);
                boolean image = false;
                try {
                    multipartFile.transferTo(savePath); // 서버에 파일저장
                    //이미지 파일의 종류라면
                    if (Files.probeContentType(savePath).startsWith("image")) {
                        image = true;
                        File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 100, 100);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                list.add(UploadResultDTO.builder()
                        .uuid(uuid)
                        .fileName(originalName)
                        .image(image)
                        .build()
                );
                model.addAttribute("list", list);
                model.addAttribute("uploadPath", uploadPath);
            });
        }
    }

    @GetMapping("/view/{fileName}")
    @ResponseBody //바디의 값을 직접 리턴한다
    public ResponseEntity<Resource> viewFileGET(@PathVariable("fileName") String fileName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @GetMapping("/remove")
    public String removeFile(@RequestParam("fileName") String fileName) {
        log.info("AAAA"+fileName);

        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName); //섬네일 파일 이름이 오게된다?
        String resourceName = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            //이미지 파일은 섬네일이랑 하나 더 있잖슴.
            removed = resource.getFile().delete();
            //이렇게하면 이미지파일의 섬네일 파일 지워지고 또?

            //섬네일이 존재한다면 - 그니까 섬네일이 이미지일 경우
            if (contentType.startsWith("image")) {
                String fileName1 = fileName.replace("s_","");
                File originalFile = new File(uploadPath + File.separator + fileName1);
                originalFile.delete();
            } //이렇게 하면 오리지널이랑 섬네일 파일 삭제된다. ㅇㅇ 먼저 그냥께 없어지고 그다음 섬네일께 지워지고

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        resultMap.put("result", removed);
        return "/upload/uploadForm";
    }
}
