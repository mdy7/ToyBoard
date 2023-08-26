package article.demo.dto;

import article.demo.domain.BoardComment;
import article.demo.domain.BoardCommentReply;
import article.demo.domain.Member;
import lombok.Builder;
import lombok.Data;

@Data
public class BoardCommentReplyDto {
    private Long id;

    private String content;
    private String createdBy;
    private BoardComment boardComment;
    private Member member;

    @Builder
    public BoardCommentReplyDto(String content, String createdBy, BoardComment boardComment, Member member) {
        this.content = content;
        this.createdBy = createdBy;
        this.boardComment = boardComment;
        this.member = member;
    }

}
