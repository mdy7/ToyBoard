package article.demo.service;

import article.demo.domain.Board;
import article.demo.domain.BoardComment;
import article.demo.dto.BoardDto;
import article.demo.repository.BoardCommentRepository;
import article.demo.repository.BoardRepository;
import article.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardCommentRepository boardCommentRepository;

    /**
     * 게시글 생성
     */
    @Transactional
    public void saveBoard(BoardDto boardDto, String username) {
        nullCheckBoard(boardDto);

        if (username == null || username.isEmpty()) {
            boardDto.boardSetting("익명", 1L);
        } else {
            boardDto.boardSetting(username, 1L);
        }

        Board board = boardDto.toEntity();

        boardRepository.save(board);
    }

    /**
     * 게시글 리스트 조회
     */
    public Page<Board> searchBoard(String searchText, String searchType, Pageable pageable) {
        Page<Board> boards;
        switch (searchType) {
            case "title":
                boards = boardRepository.findByTitleContaining(searchText, pageable);
                break;
            case "content":
                boards = boardRepository.findByContentContaining(searchText, pageable);
                break;
            case "createdBy":
                boards = boardRepository.findByCreatedByContaining(searchText, pageable);
                break;
            default:
                boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
                break;
        }
        return boards;
    }

    public List<Board> myBoarder(String username) {
        List<Board> userBoards = boardRepository.findByCreatedByOrderByIdDesc(username);
        return userBoards;
    }


    /**
     * 게시글 수정
     */
    @Transactional
    public void updateBoard(Long id, BoardDto boardDto, String username) {
        writerValidation(username,id);
        Board board = boardRepository.getBoard(id);
        board.updateBoard(boardDto.getTitle(), boardDto.getContent());
        boardRepository.save(board);
    }

    public Board updateForm(Long id, String username) {
        Board board = boardRepository.getBoard(id);

        writerValidation(username,id);

        return board;
    }


    /**
     * 게시글 삭제
     */
    @Transactional
    public void deleteBoard(Long id, String username) {
        writerValidation(username,id);

        List<BoardComment> comments = boardCommentRepository.findByBoardId(id);
        boardCommentRepository.deleteAll(comments);

        boardRepository.deleteById(id);
    }

    /**
     * 게시글 상세
     */
    @Transactional
    public Board updateVisit(Long id) {
        Board board = boardRepository.getBoard(id);

        Long countVisit = board.getCountVisit() + 1L;

        BoardDto boardDto = BoardDto.builder()
                .countVisit(countVisit)
                .build();

        board.updateVisit(boardDto.getCountVisit());
        return board;
    }


    /**
     * 검증
     */
    public void writerValidation(String username, Long id) {
        memberRepository.getUsernameBySession(username);

        Board board = boardRepository.getBoard(id);

        if (!board.getCreatedBy().equals(username) && !username.equals("admin")) {
            throw new IllegalStateException("작성자가 아닙니다.");
        }
    }

    private void nullCheckBoard(BoardDto boardDto) {
        if (boardDto.getTitle() == null || boardDto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해주세요");
        }
        if (boardDto.getContent() == null || boardDto.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요");
        }
    }
}
