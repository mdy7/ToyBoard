package article.demo.controller;


import article.demo.request.BoardCommentRequestDto;
import article.demo.request.BoardRequestDto;
import article.demo.response.ResponseDto;
import article.demo.service.BoardCommentService;
import article.demo.service.BoardService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final BoardCommentService boardCommentService;

    @ApiOperation(value = "게시글 작성")
    @PostMapping("/write")
    public ResponseDto<?> boardForm(HttpSession session,@RequestBody BoardRequestDto boardRequestDto){
        String username = (String)session.getAttribute("username");
        ResponseDto<?> responseDto = boardService.saveBoard(boardRequestDto, username);
        return responseDto;
    }

    @ApiOperation(value = "게시글 전체 조회")
    @GetMapping("/boards")
    public ResponseDto<?> getBoards() {
        return ResponseDto.success("게시물 전체 조회",boardService.getBoards());
    }

    @ApiOperation(value = "게시글 업데이트")
    @PostMapping("/{boardId}/update")
    public ResponseDto<?> boardUpdate(@PathVariable("boardId") Long boardId, @RequestBody BoardRequestDto boardRequestDto, HttpSession session) {
        String username = (String) session.getAttribute("username");
        return boardService.updateBoard(boardId, boardRequestDto, username);
    }

    @ApiOperation(value = "게시글 삭제")
    @GetMapping("/{boardId}/delete")
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
    @GetMapping("/{boardId}/comments")
    public ResponseDto<?> boardComments(@PathVariable("boardId") Long boardId){
        return boardCommentService.getComments(boardId);
    }


}
