package article.demo.service;


import article.demo.domain.Board;
import article.demo.domain.BoardComment;
import article.demo.domain.BoardCommentReply;
import article.demo.domain.Member;
import article.demo.dto.BoardCommentDto;
import article.demo.dto.BoardCommentReplyDto;
import article.demo.repository.BoardCommentReplyRepository;
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
     * 댓글 저장
     */
    @Transactional
    public void saveBoardComment(Long id, BoardCommentDto boardCommentDto,String username) {
        if (boardCommentDto.getContent() == null || boardCommentDto.getContent().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요");
        }

        Board board = boardRepository.getBoard(id);

        Member member = memberRepository.getUsernameBySession(username);

        BoardComment boardComment = BoardComment.builder()
                .createdBy(member.getUsername())
                .deleteCheck('N')
                .member(member)
                .board(board)
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

        writerValidation(username,comment);

        boardCommentRepository.delete(comment);
    }

    public void writerValidation(String username,BoardComment comment) {
        memberRepository.getUsernameBySession(username);

        if (!comment.getCreatedBy().equals(username) && !username.equals("admin")) {
            throw new IllegalStateException("댓글 작성자가 아닙니다.");
        }
    }
}
