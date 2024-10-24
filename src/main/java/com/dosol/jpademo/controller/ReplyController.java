package com.dosol.jpademo.controller;

import com.dosol.jpademo.dto.PageRequestDTO;
import com.dosol.jpademo.dto.PageResponseDTO;
import com.dosol.jpademo.dto.ReplyDTO;
import com.dosol.jpademo.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/replies")
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    //포스트맵필에 / 있으면 이 위치에 이 위치가 우리가 이 레지스터를 호출하는 함수가 된다.
    //컨슘이 제이슨 형태 밸류값이다 이말이다. 근데 이제 이게 없어도 처리된다.
    public Map<String,Long> register(
            //위에 String 이거는 리턴 타입이 저거다 라는거임
            @Valid @RequestBody ReplyDTO replyDTO,
            //리퀘스트 바디가 ReplyDTO를 보내겠다. form에 담아서 보낸다. 바디에 있는거를
            //바디에 있는걸 담아서 ReplyDTO에 담겠다.
            //register에 보내준 데이터를 @리퀘스트 바디에 있는거를 그옆에 ReplyDTO에 담겠다
            BindingResult bindingResult)throws BindException {

        log.info(replyDTO);
        //이건 값이 왔는지 확인
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        } //이거는 오류 확인 if이다

        Map<String, Long> resultMap = new HashMap<>();

        Long rno = replyService.register(replyDTO);

        resultMap.put("rno",rno);

        return resultMap;
    }
    //@Operation(summary = "Replies of Board")
    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno,
                                             PageRequestDTO pageRequestDTO){
                                            //이렇게 하면 getList에 번호랑 페이지정보 같이가
        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);
        //그럼 response에 페이징이랑 같이 들어가있음
        return responseDTO;
    }

    //@Operation(summary = "GET 방식으로 특정 댓글 조회")
    @GetMapping("/{rno}")
    public ReplyDTO getReplyDTO( @PathVariable("rno") Long rno ){

        ReplyDTO replyDTO = replyService.findById(rno);

        return replyDTO;
    }

    //   @Operation(summary =  "DELETE 방식으로 특정 댓글 삭제")
    @DeleteMapping("/{rno}")
    public Map<String,Long> remove( @PathVariable("rno") Long rno ){

        replyService.remove(rno);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }


    //@Operation(summary =  "PUT 방식으로 특정 댓글 수정")
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE )
    public Map<String,Long> modify( @PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO ){

        replyDTO.setRno(rno); //번호를 일치시킴
        //rno를 replyDTO를 세팅해주고
        replyService.modify(replyDTO);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }
}
