package com.dosol.jpademo.service;

import com.dosol.jpademo.domain.Board;
import com.dosol.jpademo.dto.BoardDTO;
import com.dosol.jpademo.dto.PageRequestDTO;
import com.dosol.jpademo.dto.PageResponseDTO;
import com.dosol.jpademo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    @Override
    public PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO) {
        log.info("GetList");
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<Board> result = boardRepository.findAll(pageable);

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toUnmodifiableList());

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public Board getBoard(Long bno) {
        log.info("Get board by id: " + bno);
        Board board = boardRepository.findById(bno).get();
        board.updateVisitcount();
        boardRepository.save(board);
        return board;
    }

    @Override
    public void saveBoard(Board board) {
        log.info("save board");
        boardRepository.save(board);
    }

    @Override
    public void updateBoard(Board board) {
        log.info("Update Board"+board);
        Board oldBoard = boardRepository.findById(board.getBno()).get();
        oldBoard.setTitle(board.getTitle());
        oldBoard.setContent(board.getContent());
        oldBoard.setWriter(board.getWriter());
        boardRepository.save(oldBoard);
    }

    @Override
    public void deleteBoard(Long bno) {
        log.info("Delete Board"+bno);
        boardRepository.deleteById(bno);
    }
}