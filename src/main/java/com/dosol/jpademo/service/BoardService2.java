package com.dosol.jpademo.service;

import com.dosol.jpademo.domain.Board;
import com.dosol.jpademo.dto.BoardDTO;
import com.dosol.jpademo.dto.PageRequestDTO;
import com.dosol.jpademo.dto.PageResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService2 {
    Long register(BoardDTO boardDTO);
    BoardDTO readOne(Long bno);
    void modify(BoardDTO boardDTO);
    void remove(Long bno);
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    //다시 컨트롤러에 갈때 DTO로 줘야함
    //모델매퍼는 프라이빗애들 이름이 다 같아야하하는데 이제 달라서 못씀.
    //그래서 이렇게 바꿔주는거임.
    default Board dtoToEntity(BoardDTO boardDTO){
        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();
        if(boardDTO.getFileNames() != null){
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            }); //이 부분 좀 어렵네. 파일네임이 스트링이 되어있는데, 그걸 uuid랑 fileName을 붙일거임.
            //그 이름을 스플릿을...그니까 이게 arr에는 uuid arr1은 원래꺼가 들어감.
        }
        return board;
    }
    default BoardDTO entityToDTO(Board board) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames = //보드DTO에 있었음.
                board.getImageSet().stream().sorted().map(boardImage ->
                //보드 이미지셋에서 가져온거 정렬하고
                                boardImage.getUuid()+"_"+boardImage.getFileName())
                //보드이미지 한개 꺼내온걸, _붙여서 리스트로 만들어서 파일네임즈 안에 넣어주는거임.
                        //그리고 새로 만든걸 boardDTO에 넣어줌
                        //그러니까 이건 넣어주는거네. 파일저장해주는거네.
                        .collect(Collectors.toList());
        boardDTO.setFileNames(fileNames);
        return boardDTO;
    }

}