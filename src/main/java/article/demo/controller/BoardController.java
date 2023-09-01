package article.demo.controller;


import article.demo.request.BoardRequestDto;
import article.demo.response.ResponseDto;
import article.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/boardForm")
    public ResponseDto<?> boardForm(HttpSession session,@RequestBody BoardRequestDto boardRequestDto){
        String username = (String)session.getAttribute("username");
        ResponseDto<?> responseDto = boardService.saveBoard(boardRequestDto, username);
        return responseDto;
    }

//    @GetMapping("/boards")
//    public List<Board> boards(){
//        return boardService.findAll();
//    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/boards")
    public ResponseDto getBoards() {
        return ResponseDto.success("게시물 전체 조회",boardService.getBoards());
    }


}
