package com.dosol.jpademo.controller;

import com.dosol.jpademo.domain.Board;
import com.dosol.jpademo.service.BoardService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/board")
public class BoardController {
    @Autowired
    private BoardService boardService;

    @GetMapping("/list")
    public void list(Model model) {
        log.info("controller list");
        model.addAttribute("BoardList", boardService.getList());
    }

    @GetMapping("/register")
    public void registerGet(){
        log.info("controller registerGet");
    }

    @PostMapping("/register")
    public String registerPost(Board board){
            boardService.saveBoard(board);
            return "redirect:/board/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(Long bno, Model model){
        Board board = boardService.getBoard(bno);
        model.addAttribute("Board", board);
    }

    @PostMapping("/modify")
    public String modify(Board board){
        boardService.updateBoard(board);
        return "redirect:/board/read";
    }

}
