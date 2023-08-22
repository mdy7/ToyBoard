package article.demo.service;

import article.demo.domain.Board;
import article.demo.dto.BoardDto;
import article.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 게시글 생성
     */
    @Transactional
    public void saveBoard(BoardDto boardDto, String username) {
        userValidation(boardDto, username);
        Board board = boardDto.toEntity();
        boardRepository.save(board);
    }

    private static void userValidation(BoardDto boardDto, String username) {
        if (username == null || username.isEmpty()) {
            boardDto.boardSetting("익명", 1L);
        } else {
            boardDto.boardSetting(username, 1L);
        }
    }

    /**
     * 게시글 리스트 조회
     */

    public Page<Board> boardSearch(String searchText,Pageable pageable) {
            return boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
        }

    public Board findById(Long id) {
        return boardRepository.findById(id).get();
    }

    public List<Board> findBoardByUsername(HttpSession session) {
        String username = (String) session.getAttribute("username");
        List<Board> userBoards = boardRepository.findByCreatedByOrderByIdDesc(username);
        return userBoards;
    }



    /**
     * 게시글 수정
     */
    @Transactional
    public void updateBoard(Long id, BoardDto boardDto) {
        Board findBoard = boardRepository.findById(id).get();
        findBoard.updateBoard(boardDto.getTitle(),boardDto.getContent());
        boardRepository.save(findBoard);
    }


    /**
     * 게시글 삭제
     */
    @Transactional
    public void deleteBoard(Long id){
        boardRepository.deleteById(id);
    }
}
