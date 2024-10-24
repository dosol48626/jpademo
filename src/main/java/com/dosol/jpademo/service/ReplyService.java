package com.dosol.jpademo.service;

import com.dosol.jpademo.domain.Reply;
import com.dosol.jpademo.dto.PageRequestDTO;
import com.dosol.jpademo.dto.PageResponseDTO;
import com.dosol.jpademo.dto.ReplyDTO;

import java.util.List;

public interface ReplyService {
    Long register(ReplyDTO replyDTO);
    ReplyDTO findById(Long rno);
    void modify(ReplyDTO replyDTO);
    void remove(Long rno);
    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
}
