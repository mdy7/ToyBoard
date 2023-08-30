package article.demo.service;


import article.demo.domain.Board;
import article.demo.domain.BoardComment;
import article.demo.domain.Member;
import article.demo.dto.BoardCommentDto;
import article.demo.repository.BoardCommentRepository;
import article.demo.repository.BoardRepository;
import article.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 댓글 작성
     */
    @Transactional
    public void saveBoardCommentParent(Long id, BoardCommentDto boardCommentDto, String username) {
        Board board = boardRepository.getBoard(id);
        Member member = memberRepository.getUsername(username);

        if (boardCommentDto.getContent() == null || boardCommentDto.getContent().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요");
        }

        BoardComment boardComment = BoardComment.builder()
                .createdBy(member.getUsername())
                .deleteCheck(false)
                .member(member)
                .board(board)
                .content(boardCommentDto.getContent())
                .build();

        boardCommentRepository.save(boardComment);
    }


    /**
     * 대댓글 작성
     */
    @Transactional
    public void saveBoardCommentChild(Long commentId, BoardCommentDto boardCommentDto, String username,Long boardId) {
        Board board = boardRepository.getBoard(boardId);

        Member member = memberRepository.getUsername(username);

        if (boardCommentDto.getContent() == null || boardCommentDto.getContent().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요");
        }

        BoardComment parentComment = boardCommentRepository.findById(commentId).orElseThrow(() ->
                new IllegalStateException("부모 댓글이 없습니다"));

        BoardComment boardComment = BoardComment.builder()
                .createdBy(member.getUsername())
                .deleteCheck(false)
                .member(member)
                .board(board)
                .parent(parentComment) // 대댓글의 경우 부모 댓글 설정
                .content(boardCommentDto.getContent())
                .build();

        boardCommentRepository.save(boardComment);
    }

    /**
     * 댓글 조회
     */
    public List<BoardComment> findCommentBoardId(Long id) {
        return boardCommentRepository.findByBoardId(id);
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public void deleteCommentById(Long commentId,String username) {
        BoardComment comment = boardCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        memberRepository.getUsername(username);

        if (!comment.getCreatedBy().equals(username) && !username.equals("admin")) {
            throw new IllegalStateException("해당 댓글 작성자가 아닙니다.");
        }

        boardCommentRepository.delete(comment);
    }

    @Transactional
    public void deleteReply(Long replyId,String username) {
        BoardComment reply = boardCommentRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("대댓글을 찾을 수 없습니다."));

        memberRepository.getUsername(username);

        if (!reply.getCreatedBy().equals(username) && !username.equals("admin")) {
            throw new IllegalStateException("해당 대댓글 작성자가 아닙니다.");
        }

        boardCommentRepository.delete(reply);
    }
}
