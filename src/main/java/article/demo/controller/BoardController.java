package article.demo.controller;


import article.demo.request.BoardCommentDto;
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
    @PostMapping("/boardForm")
    public ResponseDto<?> boardForm(HttpSession session,@RequestBody BoardRequestDto boardRequestDto){
        String username = (String)session.getAttribute("username");
        ResponseDto<?> responseDto = boardService.saveBoard(boardRequestDto, username);
        return responseDto;
    }

    @ApiOperation(value = "게시글 조회")
    @GetMapping("/boards")
    public ResponseDto<?> getBoards() {
        return ResponseDto.success("게시물 전체 조회",boardService.getBoards());
    }

    @ApiOperation(value = "게시글 업데이트")
    @PostMapping("/boardUpdate/{id}")
    public ResponseDto<?> boardUpdate(@PathVariable("id") Long id, @RequestBody BoardRequestDto boardRequestDto, HttpSession session) {
        String username = (String) session.getAttribute("username");
        return boardService.updateBoard(id, boardRequestDto, username);
    }

    @ApiOperation(value = "게시글 삭제")
    @GetMapping("/boardDelete/{id}")
    public ResponseDto<?> boardDelete(@PathVariable("id") Long id,HttpSession session) {
        String username = (String) session.getAttribute("username");
        return boardService.deleteBoard(id,username);
    }

    @ApiOperation(value = "게시글 댓글 작성")
    @PostMapping("/boardCommentParent/{id}")
    public ResponseDto<?> addComment(@PathVariable("id") Long id, @RequestBody BoardCommentDto boardCommentDto, HttpSession session) {
        String username = (String) session.getAttribute("username");
        return boardCommentService.saveBoardCommentParent(id, boardCommentDto, username);
    }

    @ApiOperation(value = "게시글 댓글 조회")
    @GetMapping("/bardComments/{id}")
    public ResponseDto<?> boardComments(@PathVariable Long id){
        return boardCommentService.getComment(id);
    }
}
