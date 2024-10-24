package com.dosol.jpademo.service;

import com.dosol.jpademo.domain.Reply;
import com.dosol.jpademo.dto.PageRequestDTO;
import com.dosol.jpademo.dto.PageResponseDTO;
import com.dosol.jpademo.dto.ReplyDTO;
import com.dosol.jpademo.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
//서비스입니다~ 하고 알려주는 어노테이션
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;
    //이거는 왜 있어야하지? 변환하기 위해서. DTO이런거를

    @Override
    public Long register(ReplyDTO replyDTO) {
        Reply reply = modelMapper.map(replyDTO, Reply.class);
        Long rno = replyRepository.save(reply).getRno();
        //Reply엔티티가 반환이 된다? 리턴이 된다?
        return rno;
    }

    @Override
        public ReplyDTO findById(Long rno) {
            Optional<Reply> replyOptional = replyRepository.findById(rno);
            Reply reply = replyOptional.orElseThrow();
            //정상적으로 못꺼냈을때, 예외처리를 해주는거임.
            return modelMapper.map(reply, ReplyDTO.class);
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        Optional<Reply> replyOptional = replyRepository.findById(replyDTO.getRno());
        //getRno로 그 번호의 게시물 긁어오는건가.
        Reply reply = replyOptional.orElseThrow();
        reply.changeText(replyDTO.getReplyText());
        replyRepository.save(reply);
    }

    @Override
    public void remove(Long rno) {
        replyRepository.deleteById(rno);
    }

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO
                        .getPage() <=0? 0: pageRequestDTO.getPage() -1,
                pageRequestDTO.getSize(),
                Sort.by("rno").ascending());
        //Sort는 스프링프레임워크 임폴트해줘야한다.
        //정렬을 하는데, 댓글을 rno로 정렬하겠다. 최근꺼 보고싶으면 디센딩, 먼저한거는 어센딩

        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);
        //bno를 가지고 pageable해서 보여주는거임

        List<ReplyDTO> dtoList =
                result.getContent().stream().map(reply -> modelMapper.map(reply, ReplyDTO.class))
                        .collect(Collectors.toList());
        //modelmapper로 변환시켜주는거지 창기가 알려준거

        return PageResponseDTO.<ReplyDTO>withAll()
                //withAll부분은 좀 더 공부해야겠다.
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}
