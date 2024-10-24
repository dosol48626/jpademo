package com.dosol.jpademo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    private Long rno;

    @NotNull
    private Long bno;
    //누구의 댓글인지 번호로 확인하는거임

    @NotEmpty
    private String replyText;

    @NotEmpty
    private String replyer;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;
    //제이슨 형태로 데이터 보내면 텍스트로 가야한다.
    //숫자는 텍스트로 바로 가지만, 날짜는 바로 안가서 이런 패턴을 만들어줘야한다.

    @JsonIgnore
    private LocalDateTime modDate;

}