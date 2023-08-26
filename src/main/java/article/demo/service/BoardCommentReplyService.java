package article.demo.service;

import article.demo.domain.BoardComment;
import article.demo.domain.BoardCommentReply;
import article.demo.domain.Member;
import article.demo.dto.BoardCommentReplyDto;
import article.demo.repository.BoardCommentReplyRepository;
import article.demo.repository.BoardCommentRepository;
import article.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardCommentReplyService {

    private final BoardCommentRepository boardCommentRepository;
    private final BoardCommentReplyRepository boardCommentReplyRepository;
    private final MemberRepository memberRepository;

    /**
     * 대댓글 저장
     */
    @Transactional
    public void saveBoardCommentReply(Long boardCommentId, BoardCommentReplyDto boardCommentReplyDto, String username) {
        if (boardCommentReplyDto.getContent() == null || boardCommentReplyDto.getContent().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요");
        }

        BoardComment boardComment = boardCommentRepository.findById(boardCommentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다"));

        Member member = memberRepository.getUsernameBySession(username);

        BoardCommentReply boardCommentReply = BoardCommentReply.builder()
                .createdBy(member.getUsername())
                .member(member)
                .boardComment(boardComment)
                .content(boardCommentReplyDto.getContent())
                .build();

        boardCommentReplyRepository.save(boardCommentReply);
    }
}
