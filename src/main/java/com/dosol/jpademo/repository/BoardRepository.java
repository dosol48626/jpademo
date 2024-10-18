package com.dosol.jpademo.repository;

import com.dosol.jpademo.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
