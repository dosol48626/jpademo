package com.dosol.jpademo.service;

import com.dosol.jpademo.domain.Board;
import com.dosol.jpademo.dto.BoardDTO;
import com.dosol.jpademo.dto.PageRequestDTO;
import com.dosol.jpademo.dto.PageResponseDTO;
import com.dosol.jpademo.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
class BoardService2Impl implements BoardService2{
    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    @Override
    public Long register(BoardDTO boardDTO) {
        //Board board = modelMapper.map(boardDTO, Board.class);
        Board board = dtoToEntity(boardDTO); //그래서 이렇게 바뀐거네. dtoToEntity로
        Long bno = boardRepository.save(board).getBno();
        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {
        //Optional<Board> result = boardRepository.findById(bno);
        Optional<Board> result = boardRepository.findByWithImages(bno);

        //해당게시물과 연관된 이미지를 다 가져오겠지.

        Board board = result.orElseThrow();
        board.updateVisitcount();
        //BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
        BoardDTO boardDTO= entityToDTO(board);

        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        //원래 파일을 가져와야 한다. 그래서 이렇게 가져옴

        Board board = result.orElseThrow();

        board.change(boardDTO.getTitle(), boardDTO.getContent());
        board.clearImages();
        //업데이트할때 이미지 파일이 바뀔 수 있으니, 그냥 이미지를 지워버리고 바꿔치기하네
        //그럼 이미지가 안바뀌면?
        if (boardDTO.getFileNames() != null) {
            for (String fileName : boardDTO.getFileNames()) {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            }
        }

        boardRepository.save(board);

    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<Board> result = boardRepository.searchAll(types,keyword, pageable);

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board,BoardDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}