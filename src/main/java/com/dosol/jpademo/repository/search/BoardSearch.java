package com.dosol.jpademo.repository.search;

import com.dosol.jpademo.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);
}
