package article.demo.controller;


import article.demo.domain.Board;
import article.demo.dto.BoardDto;
import article.demo.repository.BoardRepository;
import article.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/boardForm")
    public String boardForm(Model model) {
        return "board/boardForm";
    }

    @PostMapping("/boardForm")
    public String boardForm(HttpSession session,BoardDto boardDto){
        String username = (String)session.getAttribute("username");
        boardService.saveBoard(boardDto,username);
        return "redirect:/board/boardList";
    }

    @GetMapping("/boardList")
    public String boardList(Model model,
                            @RequestParam(required = false, defaultValue = "") String searchText,
                            @PageableDefault(page = 0, size = 10,sort = "id",direction = Sort.Direction.DESC)
                            Pageable pageable) {
        Page<Board> boards = boardService.boardSearch(searchText,pageable);

        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 1);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 3);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);
        return "board/boardList";
    }


//    @GetMapping("/boardList")
//    public String boardList(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
//                            @PageableDefault(size = 3) Pageable pageable, Model model) {
//        Page<Board> searchPage = boardService.keywordSearchPageable(keyword, pageable);
//        model.addAttribute("boards", searchPage);
//        model.addAttribute("keyword", keyword);
//        return "board/boardList";
//    }


    @GetMapping("/boardContent/{id}")
    public String boardContent(@PathVariable("id") Long id, Model model) {
        Board board = boardService.findById(id);
        model.addAttribute(board);
        return "/board/boardContent";
    }

    @GetMapping("/boardInfo")
    public String boardInfo(HttpSession session,Model model) {
        List<Board> userBoards = boardService.findBoardByUsername(session);
        model.addAttribute("userBoards", userBoards);
        return "/board/boardInfo";
    }

    @GetMapping("/boardUpdate/{id}")
    public String boardUpdateForm(@PathVariable("id") Long id,Model model) {
        Board board = boardService.findById(id);
        model.addAttribute("board", board);
        return "board/boardUpdate";
    }

    @PostMapping("/boardUpdate/{id}")
    public String boardUpdate(@PathVariable("id") Long id,BoardDto boardDto,Model model) {
        boardService.updateBoard(id,boardDto);
        return "redirect:/board/boardContent/" + id;
    }

    @GetMapping("/boardDelete/{id}")
    public String boardDelete(@PathVariable("id") Long id) {
        boardService.deleteBoard(id);
        return "redirect:/board/boardList";
    }

}
