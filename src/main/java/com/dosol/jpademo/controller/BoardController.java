package com.dosol.jpademo.controller;

import com.dosol.jpademo.domain.Board;
import com.dosol.jpademo.dto.BoardDTO;
import com.dosol.jpademo.dto.PageRequestDTO;
import com.dosol.jpademo.dto.PageResponseDTO;
import com.dosol.jpademo.dto.upload.UploadFileDTO;
import com.dosol.jpademo.dto.upload.UploadResultDTO;
import com.dosol.jpademo.service.BoardService2;
import com.dosol.jpademo.service.BoardService2;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {
    @Value("${com.dosol.jpademo.upload.path}")
    private String uploadPath; //이렇게해서 경로 잡아주는거구나

    private final BoardService2 boardService;


    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        log.info(responseDTO);
        model.addAttribute("responseDTO", responseDTO);
    }

    @GetMapping("/register")
    public void registerGET(){

    }

    @PostMapping("/register")
    public String registerPost(UploadFileDTO uploadFileDTO,
                               BoardDTO boardDTO,
//                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes){

        List<String> strFileNames = null;
        if (uploadFileDTO.getFiles() != null &&
                !uploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")){
                //겟 파일이 널과 같지 않다는것은, 파일이 올라갔으면
            strFileNames = fileUpload(uploadFileDTO);
            log.info(strFileNames.size());
        }
        boardDTO.setFileNames(strFileNames);


        log.info("board POST register.......");
//        if(bindingResult.hasErrors()) {
//            log.info("has errors.......");
//            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
//            return "redirect:/board/register";
//        }
        log.info(boardDTO);
        Long bno  = boardService.register(boardDTO);
//        redirectAttributes.addFlashAttribute("result", bno);
        return "redirect:/board/list";
    }

    private List<String> fileUpload(UploadFileDTO uploadFileDTO) {

        List<String> list = new ArrayList<>();
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
                    Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
                }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                list.add(uuid+"_"+originalName);
            });
        return list;
    }

    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){
        BoardDTO boardDTO = boardService.readOne(bno);
//        boardDTO.getVisitcount();
        log.info(boardDTO);
        model.addAttribute("dto", boardDTO);
    }
    @PostMapping("/modify")
    public String modify(UploadFileDTO uploadFileDTO,
                         PageRequestDTO pageRequestDTO,
                          @Valid BoardDTO boardDTO,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes){

        log.info("board modify post......." + boardDTO);

        List<String> strFileNames = null;
        if (uploadFileDTO.getFiles() != null &&
                !uploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")){

            List<String> fileNames = boardDTO.getFileNames();

            if (fileNames != null && fileNames.size() > 0){
                removeFile(fileNames);
            }

            strFileNames = fileUpload(uploadFileDTO);
            log.info(strFileNames.size());
            boardDTO.setFileNames(strFileNames);
            //뭐가 있을때 가져가고 없을때 가져간다는건지..
        }

        if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            redirectAttributes.addAttribute("bno", boardDTO.getBno());
            return "redirect:/board/modify?"+link;
        }

        boardService.modify(boardDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/read";
    }

    @PostMapping("/remove")
    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
        log.info("remove post.. " + boardDTO);

        List<String> fileNames = boardDTO.getFileNames();
        if (fileNames != null && fileNames.size() > 0) {
            removeFile(fileNames);
        }
        boardService.remove(boardDTO.getBno());

        //파일이 올라간 게시글을 지울때, 서버에 있는것도 같이 지워야한다.
        redirectAttributes.addFlashAttribute("result", "removed");
        return "redirect:/board/list";
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

    private void removeFile(List<String> fileNames) {
        log.info("AAAA"+fileNames.size());


        for (String fileName : fileNames) {
            log.info("fileRemove method:" + fileName);
            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName); //섬네일 파일 이름이 오게된다?
            String resourceName = resource.getFilename();

            //Map<String, Boolean> resultMap = new HashMap<>();
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
        }
    }
}