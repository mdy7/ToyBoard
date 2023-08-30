package article.demo.controller;


import article.demo.domain.Board;
import article.demo.domain.BoardComment;
import article.demo.dto.BoardCommentDto;
import article.demo.dto.BoardDto;
import article.demo.service.BoardCommentService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardCommentService boardCommentService;

    @GetMapping("/boardForm")
    public String boardForm() {
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
        List<BoardComment> comments = boardCommentService.findCommentBoardId(id);

        // 댓글별로 대댓글 조회
        Map<BoardComment, List<BoardComment>> commentRepliesMap = new HashMap<>();
        for (BoardComment comment : comments) {
            List<BoardComment> replies = comment.getChildren();
            commentRepliesMap.put(comment, replies);
        }

        model.addAttribute("commentRepliesMap", commentRepliesMap);
        model.addAttribute("comments",comments);
        model.addAttribute(board);
        return "/board/boardContent";
    }

    @PostMapping("/boardCommentParent/{id}")
    public String addComment(@PathVariable("id") Long id, BoardCommentDto boardCommentDto, HttpSession session) {
        String username = (String) session.getAttribute("username");
        boardCommentService.saveBoardCommentParent(id, boardCommentDto, username);
        return "redirect:/board/boardContent/" + id;
    }

    @PostMapping("/boardCommentChild/{commentId}")
    public String addReply(@PathVariable Long commentId,Long boardId, BoardCommentDto boardCommentDto, HttpSession session) {
        String username = (String) session.getAttribute("username");
        boardCommentService.saveBoardCommentChild(commentId, boardCommentDto, username, boardId);
        return "redirect:/board/boardContent/" + boardId;
    }

    @GetMapping("/myBoard")
    public String myBoard(HttpSession session,Model model) {
        String username = (String) session.getAttribute("username");
        List<Board> userBoards = boardService.myBoarder(username);
        model.addAttribute("userBoards", userBoards);
        return "board/boardMyBoard";
    }

    @GetMapping("/boardUpdate/{id}")
    public String boardUpdateForm(@PathVariable("id") Long id,Model model,HttpSession session) {
        String username = (String) session.getAttribute("username");
        Board board = boardService.updateForm(id,username);
        model.addAttribute("board", board);
        return "board/boardUpdate";
    }

    @PostMapping("/boardUpdate/{id}")
    public String boardUpdate(@PathVariable("id") Long id,BoardDto boardDto,HttpSession session) {
        String username = (String) session.getAttribute("username");
        boardService.updateBoard(id,boardDto,username);
        return "redirect:/board/boardContent/" + id;
    }

    @GetMapping("/boardDelete/{id}")
    public String boardDelete(@PathVariable("id") Long id,HttpSession session) {
        String username = (String) session.getAttribute("username");
        boardService.deleteBoard(id,username);
        return "redirect:/board/boardList";
    }

    @GetMapping("/boardDeleteComment/{id}")
    public String commentDelete(@PathVariable("id") Long commentId,Long boardId,HttpSession session) {
        String username = (String) session.getAttribute("username");
        boardCommentService.deleteCommentById(commentId,username);
        return "redirect:/board/boardContent/" + boardId;
    }

    @GetMapping("/deleteReply/{replyId}")
    public String replyDelete(@PathVariable Long replyId,Long boardId,HttpSession session) {
        String username = (String) session.getAttribute("username");
        boardCommentService.deleteReply(replyId,username);
        return "redirect:/board/boardContent/" + boardId;
    }

    @GetMapping("/boardLike/{boardId}")
    public String boardLike(@PathVariable Long boardId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        boardService.insert(username,boardId);
        return "redirect:/board/boardContent/" + boardId;
    }
}
