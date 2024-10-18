package com.dosol.jpademo.repository;

import com.dosol.jpademo.domain.Board;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testInsert() {
        Board board = new Board();
        board.setTitle("test");
        board.setContent("test");
        board.setWriter("writer4");
        boardRepository.save(board);

        //빌드로 이렇게 많드는 경우가 더 많음.
        //빌드클래스.빌드. 필요한 데이터 넣기
        //그리고 마지막에 빌드 해주기
        Board board1 = Board.builder()
                .title("aaa")
                .content("bbb")
                .writer("tester")
                .build();
        boardRepository.save(board1);
    }

    @Test
    public void getListTest(){
        List<Board> list = boardRepository.findAll();
        for(Board board : list){
            log.info(board);
        }
    }

    @Test
    public void getOneTest(){
        Board board = boardRepository.findById(1L).get();
        //bno를 @Id해서 그냥 이렇게 하면 된다.
        log.info(board);
    }

    @Test
    public void updateBoardTest(){
        Board board = boardRepository.findById(1L).get();
        board.setTitle("title3");
        board.setContent("content3");
        boardRepository.save(board);
    }

    @Test
    public void deleteBoardTest(){
        boardRepository.deleteById(1L);
    }
}
