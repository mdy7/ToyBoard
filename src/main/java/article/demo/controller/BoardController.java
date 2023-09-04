package article.demo.controller;


import article.demo.domain.Board;
import article.demo.request.BoardCommentRequestDto;
import article.demo.request.BoardRequestDto;
import article.demo.response.ResponseDto;
import article.demo.service.BoardCommentService;
import article.demo.service.BoardService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;
    private final BoardCommentService boardCommentService;

    @ApiOperation(value = "게시글 작성")
    @PostMapping
    public ResponseDto<?> boardWrite(HttpSession session,@RequestBody BoardRequestDto boardRequestDto){
        String username = (String)session.getAttribute("username");
        ResponseDto<?> responseDto = boardService.saveBoard(boardRequestDto, username);
        return responseDto;
    }

    @ApiOperation(value = "게시글 전체 조회")
    @GetMapping
    public ResponseDto<?> getBoards() {
        return boardService.getBoards();
    }

    @ApiOperation(value = "게시글 상세 조회")
    @GetMapping("/{boardId}")
    public ResponseDto<?> boardDetail(@PathVariable Long boardId){
        return boardService.boardDetail(boardId);
    }

    @ApiOperation(value = "게시글 수정")
    @PatchMapping("/{boardId}")
    public ResponseDto<?> boardUpdate(@PathVariable("boardId") Long boardId, @RequestBody BoardRequestDto boardRequestDto, HttpSession session) {
        String username = (String) session.getAttribute("username");
        return boardService.updateBoard(boardId, boardRequestDto, username);
    }

    @ApiOperation(value = "게시글 삭제")
    @DeleteMapping("/{boardId}")
    public ResponseDto<?> boardDelete(@PathVariable("boardId") Long boardId,HttpSession session) {
        String username = (String) session.getAttribute("username");
        return boardService.deleteBoard(boardId,username);
    }

    @ApiOperation(value = "게시글 댓글 작성")
    @PostMapping("/{boardId}/comment")
    public ResponseDto<?> addComment(@PathVariable("boardId") Long boardId, @RequestBody BoardCommentRequestDto boardCommentRequestDto, HttpSession session) {
        String username = (String) session.getAttribute("username");
        return boardCommentService.saveBoardCommentParent(boardId,boardCommentRequestDto, username);
    }

    @ApiOperation(value = "게시글 댓글 조회")
    @GetMapping("/{boardId}/comment")
    public ResponseDto<?> boardComments(@PathVariable("boardId") Long boardId){
        return boardCommentService.getComments(boardId);
    }

    @ApiOperation(value = "게시글 댓글 삭제")
    @DeleteMapping("/{boardId}/comment/{commentId}")
    public ResponseDto<?> commentDelete(@PathVariable("commentId") Long commentId, @PathVariable String boardId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        return boardCommentService.deleteCommentById(commentId,username);
    }

    @ApiOperation(value = "게시글 좋아요")
    @GetMapping("/{boardId}/boardLike")
    public ResponseDto<?> boardLike(@PathVariable Long boardId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        return boardService.insertLike(username,boardId);
    }

    @ApiOperation(value = "게시글 ")
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
}
