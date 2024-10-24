package com.dosol.jpademo.repository;

import com.dosol.jpademo.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Query("select r from Reply r where r.board.bno=:bno")
    //여기에 셀렉트는 객체의 것을 적어야한다.
    Page<Reply> listOfBoard(Long bno, Pageable pageable);

    void deleteByBoard_Bno(Long bno);
    //이렇게하면 알아서 번호 찾아서 지워짐 _ 이게 중요함 Board_Bno의 관련된 모든 댓글을 지우겠다.
}
