package com.dosol.jpademo.service;

import com.dosol.jpademo.domain.Board;
import com.dosol.jpademo.dto.BoardDTO;
import com.dosol.jpademo.dto.PageRequestDTO;
import com.dosol.jpademo.dto.PageResponseDTO;

import java.util.List;

public interface BoardService {
    PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO);
    Board getBoard(Long bno);
    void saveBoard(Board board);
    void updateBoard(Board board);
    void deleteBoard(Long bno);
}
