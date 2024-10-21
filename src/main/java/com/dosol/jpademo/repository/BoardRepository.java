package com.dosol.jpademo.repository;

import com.dosol.jpademo.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("select  b from Board b where b.title like concat('%',:keyward,'%')order by b.bno desc")
    Page<Board> searchAll(String keyward, Pageable pageable);
    //Page<Board> findByTitle(String title, Pageable pageable);

}
