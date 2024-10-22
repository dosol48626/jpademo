package com.dosol.jpademo.repository.search;

import com.dosol.jpademo.domain.Board;
import com.dosol.jpademo.domain.QBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
    //super클래스로, 하나를 받아주는게 있어야 쓸 수 있음. ??
    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        //쿼리dsl에서는 쿼리보드라는거임. q보드를 q보드 형으로 객체를 만들어주면 된다. 정해진거임 외우셈
        //갑자기 에러 떳다가 사라졌네?? 에러뜨면 다시 클린하고 컴파일 자바 해주면 고쳐짐
        JPQLQuery<Board> query = from(board);
        //BooleanBuilder booleanBuilder = new BooleanBuilder();
        if ((types != null && types.length > 0) && (keyword != null)) {
            //배열의 갯수가 0보다 크면 타입이 있다는거지 그리고 널이 아니면 작업할거고
            BooleanBuilder builder = new BooleanBuilder();
            //불리안으로 또 한번 가르고
            for (String type : types) {
                //타입이 t c w인가를 골라서
                switch (type) {
                    case "t":
                        builder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        builder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        builder.or(board.writer.contains(keyword));
                        break;
                        //여기서 이제 값에 맞는거 골라야함
                }
            }
            query.where(builder);
            //다 하면 원래 쿼리에 빌더를 넣는거임
        }
        query.where(board.bno.gt(0l));
        //쿼리.웨어는 보드 비엔오 지티. 보드가 0보다 큰가
        this.getQuerydsl().applyPagination(pageable, query);
        //이렇게 해서 페이지블이랑 적용?
        List<Board> list = query.fetch();
        //이렇게 완성된 쿼리를 패치. 쿼리의 결과를 끄내온다. 그걸 보드 리스트에 넣는다
        long total = query.fetchCount();
        //그리고 토탈 카운트도 얻고
        return  new PageImpl<>(list, pageable, total);
        //그걸 PageImpl이라는 이름으로 list pageable total값을 보내준다.
    }
}
