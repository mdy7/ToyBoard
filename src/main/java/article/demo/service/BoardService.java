package article.demo.service;

import article.demo.domain.Board;
import article.demo.domain.BoardComment;
import article.demo.domain.BoardLike;
import article.demo.domain.Member;
import article.demo.request.BoardRequestDto;
import article.demo.repository.BoardCommentRepository;
import article.demo.repository.BoardLikeRepository;
import article.demo.repository.BoardRepository;
import article.demo.repository.MemberRepository;
import article.demo.request.PageRequestDto;
import article.demo.response.BoardResponseDto;
import article.demo.response.PageResponseDto;
import article.demo.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


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
    public ResponseDto<?> saveBoard(BoardRequestDto boardRequestDto, String username) {
        Member member = memberRepository.getMemberByUsername(username);
        nullCheckBoard(boardRequestDto);

        boardRequestDto.updateCreateBy(member.getUsername(), 1L, 0L, member);

        Board board = Board.builder()
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .createdBy(boardRequestDto.getCreatedBy())
                .member(boardRequestDto.getMember())
                .countVisit(boardRequestDto.getCountVisit())
                .likeCount(boardRequestDto.getLikeCount())
                .build();

        boardRepository.save(board);

        return ResponseDto.success(
                "게시글 생성 성공",
                BoardResponseDto.builder()
                        .id(board.getId())
                        .createBy(board.getCreatedBy())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .build()
        );
    }

    /**
     * 게시글 조회
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

    public ResponseDto<?> getBoards() {
        List<Board> boards = boardRepository.findAll();
        List<BoardResponseDto> boardDto = boards.stream() // 스트림으로 변환 (스트림이란? 여러 요소를 연속적으로 처리할 수 있는 데이터 흐름)
                .map(BoardResponseDto::toDto) // 각 board 객체를 dto로 변환
                .collect(Collectors.toList()); // 리스트로 수집?
        return ResponseDto.success("게시물 전체 조회", boardDto);
    }

    public ResponseDto<?> pagingBoards(PageRequestDto pageRequestDto) {
        Pageable pageable = PageRequest.of(pageRequestDto.getPage(), pageRequestDto.getSize());
        Page<Board> boardPage = boardRepository.findAll(pageable);

        List<BoardResponseDto> pageBoards = boardPage.getContent()
                .stream()
                .map(BoardResponseDto::toDto)
                .collect(Collectors.toList());

        return ResponseDto.success("게시물 페이징 조회",
                PageResponseDto.builder()
                        .nowPage(boardPage.getPageable().getPageNumber())
                        .startPage(boardPage.getPageable().getPageNumber())
                        .endPage(boardPage.getTotalPages() - 1)
                        .totalPages(boardPage.getTotalPages())
                        .boards(pageBoards)
                        .build()
        );
    }

    public List<Board> myBoarder(String username) {
        return boardRepository.findByCreatedByOrderByIdDesc(username);
    }


    /**
     * 게시글 수정
     */
    @Transactional
    public ResponseDto<?> updateBoard(Long boardId, BoardRequestDto boardRequestDto, String username) {
        Board board = validationWriter(username, boardId);

        board.updateBoard(boardRequestDto.getTitle(), boardRequestDto.getContent());

        boardRepository.save(board);

        return ResponseDto.success(
                "게시글 수정 성공",
                BoardResponseDto.builder()
                        .id(board.getId())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .build()
        );
    }

    public Board updateForm(Long id, String username) {
        Board board = validationWriter(username, id);
        return board;
    }


    /**
     * 게시글 삭제
     */
    @Transactional
    public ResponseDto<?> deleteBoard(Long boardId, String username) {
        Board board = validationWriter(username, boardId);

        List<BoardComment> comments = boardCommentRepository.findByBoardId(board.getId());
        boardCommentRepository.deleteAll(comments); // 댓글 먼저 삭제후 게시글 삭제

        boardRepository.deleteById(board.getId());

        return ResponseDto.success("게시글 삭제 성공", null);
    }

    /**
     * 게시글 조회수
     */
    @Transactional
    public Board updateVisit(Long id) {
        Board board = boardRepository.getBoard(id);

        Long countVisit = board.getCountVisit();

        BoardRequestDto boardRequestDto = BoardRequestDto.builder()
                .countVisit(countVisit + 1L)
                .build();

        board.updateVisit(boardRequestDto.getCountVisit());
        return board;
    }

    @Transactional
    public ResponseDto<?> boardDetail(Long boardId) {
        Board board = boardRepository.getBoard(boardId);

        // 조회수 증가
        Long countVisit = board.getCountVisit();

        BoardRequestDto boardRequestDto = BoardRequestDto.builder()
                .countVisit(countVisit + 1L)
                .build();

        board.updateVisit(boardRequestDto.getCountVisit());

        return ResponseDto.success("게시글 상세 조회", BoardResponseDto.toDto(board)
        );
    }

    /**
     * 좋아요 눌렀을때
     */
    @Transactional
    public ResponseDto<?> insertLike(String username, Long boardId) {
        Member member = memberRepository.getMemberByUsername(username);
        Board board = boardRepository.getBoard(boardId);

        if (boardLikeRepository.findByMemberAndBoard(member, board).isPresent()) {
            throw new IllegalStateException("이미 좋아요 누른 게시글입니다.");
        }

        BoardLike boardLike = BoardLike.builder()
                .member(member)
                .board(board)
                .build();

        boardLikeRepository.save(boardLike);

        // 좋아요 수 증가
        board.updateLike(board.getLikeCount() + 1L);
        boardRepository.save(board);

        return ResponseDto.success("좋아요 성공", null);
    }


    /**
     * 검증
     */
    public Board validationWriter(String username, Long id) {
        Board board = boardRepository.getBoard(id);

        memberRepository.getMemberByUsername(username);

        if (!board.getCreatedBy().equals(username)) {
            throw new IllegalStateException("작성자가 아닙니다.");
        }
        return board;
    }

    private void nullCheckBoard(BoardRequestDto boardRequestDto) {
        // null 공백 체크
        if (boardRequestDto.getTitle() == null || boardRequestDto.getTitle().isEmpty()) {
            throw new IllegalStateException("제목을 입력해주세요");
        }
        if (boardRequestDto.getContent() == null || boardRequestDto.getContent().trim().isEmpty()) {
            throw new IllegalStateException("내용을 입력해주세요");
        }
    }

}
