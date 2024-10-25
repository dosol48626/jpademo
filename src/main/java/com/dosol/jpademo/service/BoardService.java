//package com.dosol.jpademo.service;
//
//import com.dosol.jpademo.domain.Board;
//import com.dosol.jpademo.dto.BoardDTO;
//import com.dosol.jpademo.dto.PageRequestDTO;
//import com.dosol.jpademo.dto.PageResponseDTO;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public interface BoardService {
//    PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO);
//    Board getBoard(Long bno);
//    void saveBoard(Board board);
//    void updateBoard(Board board);
//    void deleteBoard(Long bno);
//    //어라 이거 레지스트가 있나???
//
//    default Board dtoToEntity(BoardDTO boardDTO) {
//
//        Board board = Board.builder()
//                .bno(boardDTO.getBno())
//                .title(boardDTO.getTitle())
//                .content(boardDTO.getContent())
//                .writer(boardDTO.getWriter())
//                .build();
//
//        if (boardDTO.getFileNames() != null) {  //만약 파일 네임을 넣었으면
//            boardDTO.getFileNames().forEach(fileName -> {
//                String[] arr = fileName.split("_");
//                board.addImage(arr[0], arr[1]); //0번은 uuid가 되고 1번은 파일 네임이 되어서 들어간다.
//            });
//        }
//        return board;
//    }
//
//    default BoardDTO entityToDTO(Board board) {
//        BoardDTO boardDTO = BoardDTO.builder()
//                .bno(board.getBno())
//                .title(board.getTitle())
//                .content(board.getContent())
//                .writer(board.getWriter())
//                .regDate(board.getRegDate())
//                .modDate(board.getModDate())
//                .build();
//
//        List<String> fileNames =
//                board.getImageSet().stream().sorted().map(boardImage ->
//                        boardImage.getUuid()+"_"+boardImage.getFileName())
//                        .collect(Collectors.toList());
//        boardDTO.setFileNames(fileNames);
//        return boardDTO;
//    }
//}
