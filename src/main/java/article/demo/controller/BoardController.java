package article.demo.controller;


import article.demo.request.BoardCommentRequestDto;
import article.demo.request.BoardRequestDto;
import article.demo.request.PageRequestDto;
import article.demo.response.BoardCommentResponseDto;
import article.demo.response.BoardResponseDto;
import article.demo.response.PageResponseDto;
import article.demo.response.ResponseDto;
import article.demo.service.BoardCommentService;
import article.demo.service.BoardService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final BoardCommentService boardCommentService;

    @ApiOperation(value = "게시글 작성")
    @PostMapping
    public ResponseDto<BoardResponseDto> boardWrite(HttpSession session, @RequestBody BoardRequestDto boardRequestDto){
        String username = (String)session.getAttribute("username");
        return boardService.saveBoard(boardRequestDto, username);
    }

    @ApiOperation(value = "게시글 전체 조회")
    @GetMapping
    public ResponseDto<List<BoardResponseDto>> getBoards() {
        return boardService.allBoards();
    }

    @ApiOperation(value = "게시글 페이징하여 조회")
    @GetMapping("/paging")
    public ResponseDto<PageResponseDto> pagingBoards(@RequestBody PageRequestDto pageRequestDto) {
        return boardService.pagingAllBoards(pageRequestDto);
    }

    @ApiOperation(value = "게시글 검색으로 조회")
    @GetMapping("/board-search")
    public ResponseDto<PageResponseDto> boardList(
            @RequestParam(required = false, defaultValue = "") String searchText,
            @RequestParam(required = false, defaultValue = "") String searchType,
            @RequestBody PageRequestDto pageRequestDto) {
        return boardService.searchBoard(searchText, searchType, pageRequestDto);

    }

    @ApiOperation(value = "게시글 상세 조회")
    @GetMapping("/{boardId}")
    public ResponseDto<BoardResponseDto> boardDetail(@PathVariable Long boardId){
        return boardService.boardDetail(boardId);
    }


    @ApiOperation(value = "게시글 수정")
    @PatchMapping("/{boardId}")
    public ResponseDto<BoardResponseDto> boardUpdate(@PathVariable("boardId") Long boardId,
                                      @RequestBody BoardRequestDto boardRequestDto, HttpSession session) {
        String username = (String) session.getAttribute("username");
        return boardService.updateBoard(boardId, boardRequestDto, username);
    }

    @ApiOperation(value = "게시글 삭제")
    @DeleteMapping("/{boardId}")
    public ResponseDto<Void> boardDelete(@PathVariable("boardId") Long boardId,HttpSession session) {
        String username = (String) session.getAttribute("username");
        return boardService.deleteBoard(boardId,username);
    }

    @ApiOperation(value = "게시글 댓글 작성")
    @PostMapping("/{boardId}/comment")
    public ResponseDto<BoardCommentResponseDto> addComment(@PathVariable("boardId") Long boardId,
                                                           @RequestBody BoardCommentRequestDto boardCommentRequestDto, HttpSession session) {
        String username = (String) session.getAttribute("username");
        return boardCommentService.saveBoardComment(boardId,boardCommentRequestDto, username);
    }

    @ApiOperation(value = "게시글 댓글 조회")
    @GetMapping("/{boardId}/comment")
    public ResponseDto<List<BoardCommentResponseDto>> boardComments(@PathVariable("boardId") Long boardId){
        return boardCommentService.findComments(boardId);
    }

    @ApiOperation(value = "게시글 댓글 삭제")
    @DeleteMapping("/{boardId}/comment/{commentId}")
    public ResponseDto<Void> commentDelete(@PathVariable("commentId") Long commentId,
                                        @PathVariable String boardId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        return boardCommentService.deleteCommentById(commentId,username);
    }

    @ApiOperation(value = "게시글 좋아요")
    @GetMapping("/{boardId}/boardLike")
    public ResponseDto<Void> boardLike(@PathVariable Long boardId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        return boardService.insertLike(username,boardId);
    }
}
