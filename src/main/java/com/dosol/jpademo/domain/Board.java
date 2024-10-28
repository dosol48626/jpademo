package com.dosol.jpademo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.awt.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@ToString(exclude = "imageSet")
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

    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }

    @OneToMany(mappedBy = "board",     //보드와 맵핑을 시키겠습니다. 라는 뜻
            fetch = FetchType.LAZY, //계속 불러오는게 아니고 게으르게 천천히 가지고 와라
                cascade = {CascadeType.ALL}, //케스케이드는 왜 필요한가 - 게시글을 지웠는데 이미지가 있으면 이상하니 같이 지워지게 해주는거임
                orphanRemoval = true) //부모의 종속되어 있기때문에 부모가 삭제되면 같이 지워져라 는 의미임
    //이거 없이 하면 연관 관계 매핑이 되어있기때문에?? 매니투원 해도 1toM으로 되는거다. 그래서 안쓰면 에러뜬다?
    @Builder.Default
    @BatchSize(size = 20)
    private Set<BoardImage> imageSet = new HashSet<>();

    //이거는 필드 안잡힌다.
    //이미지셋이라는 필드를 만듬. 이 필드는 mappedBy 보드와 맵핑된다. 보드이미지는 필요할때만 필요함


    public void addImage(String uuid, String fileName) {

        //이미지가 있으면 uuid랑 파일네임 받아서 작업하고 add한다.
        BoardImage image = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this) // 현재 여기에 board가 있기에 이리된다
                .ord(imageSet.size())
                .build(); //이 빌드에 의해서 내가 가지고 싶었던 image가 생성이 된다.
        imageSet.add(image);
        //addImage 이거 호출될때마다 내가 원하는거 만들어서 추가해준다 이 말이다.
    } //근데 이걸 왜 레파지토리에서 안만들까?

    public void clearImages() {
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));
        this.imageSet.clear();
    }
}
