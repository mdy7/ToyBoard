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
        List<BoardCommentResponseDto> boardCommentResponseDto = new ArrayList<>();

        for (BoardComment s : boardComments) {
            BoardCommentResponseDto dto = BoardCommentResponseDto.toDto(s);
            boardCommentResponseDto.add(dto); //
        }
        return ResponseDto.success("댓글 조회 성공" ,boardCommentResponseDto);
    }


    /**
     * 댓글 삭제
     */
    @Transactional
    public ResponseDto<?> deleteCommentById(Long commentId,String username) {
        BoardComment comment = boardCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("해당 댓글이 존재하지 않습니다."));

        memberRepository.getMemberByUsername(username);

        if (!comment.getCreatedBy().equals(username)) {
            throw new IllegalStateException("해당 댓글 작성자가 아닙니다.");
        }

        boardCommentRepository.delete(comment);
        return ResponseDto.success("댓글 삭제 성공",null);
    }
}
