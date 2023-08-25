package article.demo.controller;


import article.demo.domain.Board;
import article.demo.dto.BoardDto;
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
                            @RequestParam(required = false, defaultValue = "") String searchType,
                            @PageableDefault(page = 0, size = 10,sort = "id",direction = Sort.Direction.DESC)
                            Pageable pageable) {
        Page<Board> boards = boardService.searchBoard(searchText,searchType,pageable);

        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 1);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 3);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);
        return "board/boardList";
    }

    @GetMapping("/boardContent/{id}")
    public String boardContent(@PathVariable("id") Long id, Model model) {
        Board board = boardService.updateVisit(id);
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
    public String boardUpdateForm(@PathVariable("id") Long id,Model model,HttpSession session) {
        Board board = boardService.findById(id,session);
        model.addAttribute("board", board);
        return "board/boardUpdate";
    }

    @PostMapping("/boardUpdate/{id}")
    public String boardUpdate(@PathVariable("id") Long id,BoardDto boardDto,HttpSession session,Model model) {
        boardService.updateBoard(id,boardDto,session);
        return "redirect:/board/boardContent/" + id;
    }

    @GetMapping("/boardDelete/{id}")
    public String boardDelete(@PathVariable("id") Long id,HttpSession session) {
        boardService.deleteBoard(id,session);
        return "redirect:/board/boardList";
    }

}
