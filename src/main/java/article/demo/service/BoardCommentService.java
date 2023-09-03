package article.demo.service;


import article.demo.domain.Board;
import article.demo.domain.BoardComment;
import article.demo.domain.Member;
import article.demo.request.BoardCommentRequestDto;
import article.demo.repository.BoardCommentRepository;
import article.demo.repository.BoardRepository;
import article.demo.repository.MemberRepository;
import article.demo.response.BoardCommentResponseDto;
import article.demo.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * 댓글 대댓글 작성
     */

    @Transactional
    public ResponseDto<?> saveBoardCommentParent(Long boardId,BoardCommentRequestDto boardCommentRequestDto, String username) {
        Board board = boardRepository.getBoard(boardId);
        Member member = memberRepository.getMemberByUsername(username);

        nullCheckCommentForm(boardCommentRequestDto, "댓글 내용을 입력해주세요");

        if (boardCommentRequestDto.getParent() == null) {
            // 부모 댓글 작성
            BoardComment boardComment = BoardComment.builder()
                    .createdBy(member.getUsername())
                    .deleteCheck(false)
                    .member(member)
                    .board(board)
                    .parent(null) // 대댓글이 아니므로 parent 필드는 null로 설정
                    .content(boardCommentRequestDto.getContent())
                    .build();

            boardCommentRepository.save(boardComment);

            return ResponseDto.success(
                    "댓글 작성 성공",
                    BoardCommentResponseDto.toDto(boardComment)
            );
        } else {
            BoardComment parentComment = boardCommentRepository.findById(boardCommentRequestDto.getParent().getId()).orElseThrow(() ->
                    new IllegalStateException("부모 댓글이 없습니다"));
            //자식댓글
            BoardComment boardComment = BoardComment.builder()
                    .createdBy(member.getUsername())
                    .deleteCheck(false)
                    .member(member)
                    .board(board)
                    .parent(parentComment) // 대댓글의 경우 부모 댓글 설정
                    .content(boardCommentRequestDto.getContent())
                    .build();

            boardCommentRepository.save(boardComment);

            return ResponseDto.success(
                    "대댓글 작성 성공",
                    BoardCommentResponseDto.toDto(boardComment)
            );
        }
    }


    /**
     * 대댓글 작성
     */
    @Transactional
    public BoardComment saveBoardCommentChild(Long commentId, BoardCommentRequestDto boardCommentRequestDto, String username, Long boardId) {
        Board board = boardRepository.getBoard(boardId);

        Member member = memberRepository.getMemberByUsername(username);

        nullCheckCommentForm(boardCommentRequestDto, "대댓글 내용을 입력해주세요");

        BoardComment parentComment = boardCommentRepository.findById(commentId).orElseThrow(() ->
                new IllegalStateException("부모 댓글이 없습니다"));

        BoardComment boardComment = BoardComment.builder()
                .createdBy(member.getUsername())
                .deleteCheck(false)
                .member(member)
                .board(board)
                .parent(parentComment) // 대댓글의 경우 부모 댓글 설정
                .content(boardCommentRequestDto.getContent())
                .build();

        return boardCommentRepository.save(boardComment);
    }

    private static void nullCheckCommentForm(BoardCommentRequestDto boardCommentRequestDto, String message) {
        if (boardCommentRequestDto.getContent() == null || boardCommentRequestDto.getContent().isEmpty()) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * 댓글 조회
     */
    public ResponseDto<?> getComments(Long boardId) {
        boardRepository.getBoard(boardId);
        List<BoardComment> boardComments = boardCommentRepository.findByBoardId(boardId);
        List<BoardCommentResponseDto> boardCommentResponseDos = new ArrayList<>();

        for (BoardComment s : boardComments) {
            BoardCommentResponseDto dto = BoardCommentResponseDto.toDto(s);
            boardCommentResponseDos.add(dto); //
        }
        return ResponseDto.success("댓글 조회 성공" ,boardCommentResponseDos);
    }


    /**
     * 댓글 삭제
     */
    @Transactional
    public void deleteCommentById(Long commentId,String username) {
        BoardComment comment = boardCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        memberRepository.getMemberByUsername(username);

        if (!comment.getCreatedBy().equals(username) && !username.equals("admin")) {
            throw new IllegalStateException("해당 댓글 작성자가 아닙니다.");
        }

        boardCommentRepository.delete(comment);
    }

    @Transactional
    public void deleteReply(Long replyId,String username) {
        BoardComment reply = boardCommentRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("대댓글을 찾을 수 없습니다."));

        memberRepository.getMemberByUsername(username);

        if (!reply.getCreatedBy().equals(username) && !username.equals("admin")) {
            throw new IllegalStateException("해당 대댓글 작성자가 아닙니다.");
        }

        boardCommentRepository.delete(reply);
    }
}
