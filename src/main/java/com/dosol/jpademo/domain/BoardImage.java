package com.dosol.jpademo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardImage implements Comparable<BoardImage>{

    @Id
    private String uuid;
    private String fileName;
    private int ord;

    @ManyToOne
    private Board board;
    //이렇게하면 폴잉키가 생긴다 보드bno


    @Override
    public int compareTo(BoardImage other) {
        return this.ord - other.ord;
    }

    public void changeBoard(Board board) {
        this.board = board;
    }
}
//이미지 한개를 검색하는 경우는 없음. 그래서 레파지토리를 굳이 안만든다. 그럼 어떻게 검색을 하나? 보드레파지토리에서 검색한다.