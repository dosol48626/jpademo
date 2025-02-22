package com.dosol.jpademo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 3;

    private String type; // 검색의 종류 t,c, w, tw, tc, twc
    private String keyword;

    public String[] getTypes(){
        //문자열 스트링으로 반환한다.
        if(type == null || type.isEmpty()){
            return null;
        }
        return type.split("");
        //글자 한자 한자 나눈다는거임 "" 이게. 띄어쓰기도 없는게
    }

    public Pageable getPageable(String...props) {
        //probs에 bno받아옴
        return PageRequest.of(this.page -1, this.size, Sort.by(props).descending());
    } //한페이지의 레코드를 가져오는 역할을 하게 된다.

    private String link;

    public String getLink() {

        if(link == null){
            StringBuilder builder = new StringBuilder();

            builder.append("page=" + this.page);

            builder.append("&size=" + this.size);


            if(type != null && type.length() > 0){
                builder.append("&type=" + type);
            }

            if(keyword != null){
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword,"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                }
            }
            link = builder.toString();
        }

        return link;
    }



}