package com.dosol.jpademo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1024)
    private String content;

    @Column(nullable = false, length = 50)
    private String writer;

    @ColumnDefault("0")
    private int visitcount;

    public void updateVisitcount() {
        this.visitcount++;
        //이거만 있으면 추가가 된다고??
    }
}
