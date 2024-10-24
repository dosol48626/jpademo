package com.dosol.jpademo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Reply", indexes = {
        @Index(name = "idx_reply_board_bno", columnList = "board_bno")
        //index를 만들거다 저 이름으로 칼럼리스트는 보드번호
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
//ToString할때 보드 빼고 해라
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
    //필드로 보드를 넣음. 이 보드 연관관계는 매니두원

    private String replyText;

    private String replyer;

    public void changeText(String text) {
        this.replyText = text;
    }
}
