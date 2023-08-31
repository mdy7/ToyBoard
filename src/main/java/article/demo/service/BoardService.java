package article.demo.service;

import article.demo.domain.Board;
import article.demo.domain.BoardComment;
import article.demo.domain.BoardLike;
import article.demo.domain.Member;
import article.demo.request.BoardDto;
import article.demo.repository.BoardCommentRepository;
import article.demo.repository.BoardLikeRepository;
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
    private final BoardLikeRepository boardLikeRepository;

    /**
     * 게시글 생성
     */
    @Transactional
    public void saveBoard(BoardDto boardDto, String username) {
        Member member = memberRepository.getMemberByUsername(username);
        memberRepository.getMemberByUsername(username);
        nullCheckBoard(boardDto);

        boardDto.updateCreateBy(member.getUsername(), 1L);

        Board board = Board.builder()
                        .title(boardDto.getTitle())
                        .content(boardDto.getContent())
                        .createdBy(boardDto.getCreatedBy())
                        .member(member)
                        .countVisit(boardDto.getCountVisit())
                        .build();

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
        return boardRepository.findByCreatedByOrderByIdDesc(username);
    }


    /**
     * 게시글 수정
     */
    @Transactional
    public void updateBoard(Long id, BoardDto boardDto, String username) {
        Board board = boardRepository.getBoard(id);
        writerValidation(username,id);
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
        boardCommentRepository.deleteAll(comments); // 댓글 먼저 삭제후 게시글 삭제

        boardRepository.deleteById(id);
    }

    /**
     * 게시글 조회수
     */
    @Transactional
    public Board updateVisit(Long id) {
        Board board = boardRepository.getBoard(id);

        Long countVisit = board.getCountVisit();

        BoardDto boardDto = BoardDto.builder()
                .countVisit(countVisit + 1L)
                .build();

        board.updateVisit(boardDto.getCountVisit());
        return board;
    }

    /**
     * 좋아요 눌렀을때
     */
    @Transactional
    public void insert(String username,Long boardId) {
        Member member = memberRepository.getMemberByUsername(username);
        Board board = boardRepository.getBoard(boardId);

        if (boardLikeRepository.findByMemberAndBoard(member, board).isPresent()){
            throw new IllegalStateException("이미 추천한 게시글입니다.");
        }

        BoardLike boardLike = BoardLike.builder()
                .member(member)
                .board(board)
                .build();

        boardLikeRepository.save(boardLike);

        // 좋아요 수 증가
        board.updateLike(board.getLikeCount() + 1L);
        boardRepository.save(board);
    }


    /**
     * 검증
     */
    public void writerValidation(String username, Long id) {
        Board board = boardRepository.getBoard(id);

        memberRepository.getMemberByUsername(username);

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
