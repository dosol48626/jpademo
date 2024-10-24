package com.dosol.jpademo.controller;

import com.dosol.jpademo.domain.Board;
import com.dosol.jpademo.dto.PageRequestDTO;
import com.dosol.jpademo.service.BoardService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//@Controller
//@Log4j2
//@RequestMapping("/board")
//public class BoardController {
//    @Autowired
//    private BoardService boardService;
//
//    @GetMapping("/list")
//    public void list(Model model) {
//        log.info("controller list");
//        model.addAttribute("BoardList", boardService.getList());
//    }
//
//    @GetMapping("/register")
//    public void registerGet(){
//        log.info("controller registerGet");
//    }
//
//    @PostMapping("/register")
//    public String registerPost(Board board){
//            boardService.saveBoard(board);
//            return "redirect:/board/list";
//    }
//
//    @GetMapping({"/read", "/modify"})
//    public void read(Long bno, Model model){
//        Board board = boardService.getBoard(bno);
//        model.addAttribute("Board", boardService.getBoard(bno));
//    }
//
//    @PostMapping("/modify")
//    public String modify(Board board, RedirectAttributes redirectAttributes){
//        Board oldBoard = boardService.getBoard(board.getBno());
//        oldBoard.setTitle(board.getTitle());
//        oldBoard.setContent(board.getContent());
//        oldBoard.setWriter(board.getWriter());
//        boardService.updateBoard(oldBoard);
//        redirectAttributes.addAttribute("bno", oldBoard.getBno());
//        return "redirect:/board/read";
//    }
//
//    @GetMapping("/delete")
//    public String delete(Long bno){
//        boardService.deleteBoard(bno);
//        return "redirect:/board/list";
//    }
//}

@Controller
@Log4j2
@RequestMapping("/board")
public class BoardController {
    @Autowired
    private BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        //요청된 정보가 DTO로 왔는데 왜 Model이 있냐.
        //빈 공간에 그릇을 넣어준다?
        //빈그릇을 꽉 차서 보여준다?
        log.info("controller list");
        model.addAttribute("responseDTO", boardService.getList(pageRequestDTO));
    }
    @GetMapping("/register")
    public void registerGet() {
        log.info("controller registerGet");
    }
    @PostMapping("/register")
    public String registerPost(Board board) {
        log.info("controller registerPost");
        boardService.saveBoard(board);
        return "redirect:/board/list";
    }
    @GetMapping({"/read","/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model) {
        log.info("controller read"+bno);
        model.addAttribute("dto", boardService.getBoard(bno));
    }
    @PostMapping("/modify")
    public String modify(Board board, PageRequestDTO pageRequestDTO,
                         RedirectAttributes redirectAttributes) {
        log.info("controller modify"+board);
        boardService.updateBoard(board);
        redirectAttributes.addAttribute("bno", board.getBno());
        return "redirect:/board/read";
    }
    @PostMapping("/remove")
    public String remove(Board board) {
        log.info("controller remove"+board);
        boardService.deleteBoard(board.getBno());
        return "redirect:/board/list";
    }
}