package com.dosol.jpademo.repository;

import com.dosol.jpademo.domain.Board;
import com.dosol.jpademo.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {
    //@Query("select b from Board b where b.title like concat('%',:keyward,'%')order by b.bno desc")
    //Page<Board> searchAll(String keyward, Pageable pageable);
    //BoardSearch 이거 있으니 이제 필요없음

    //그냥 searchAll하면 뭘 해라는건지 몰라서 @Query 이런거 적어줘야함.
    //Page<Board> findByTitle(String title, Pageable pageable);
    //findByTitle은 필드명이랑 같으니까 작동 함.
    //이렇게 자동으로 만들어지는게 jpa특징임.
    //다 해주지는 못하니까 필요에 따라 만들어주면 된다.
}
