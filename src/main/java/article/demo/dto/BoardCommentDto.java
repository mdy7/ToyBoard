package article.demo.dto;

import article.demo.domain.Board;
import article.demo.domain.BoardComment;
import article.demo.domain.Member;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class BoardCommentDto {
    private Long id;
    private String content;
    private String createdBy;
    private Character deleteCheck;

    private Member member;
    private Board board;


    @Builder
    public BoardCommentDto(String content, String createdBy, Character deleteCheck, Board board, Member member) {
        this.content = content;
        this.createdBy = createdBy;
        this.deleteCheck = deleteCheck;
        this.board = board;
        this.member = member;
    }


    public BoardComment toEntity() {
        return BoardComment.builder()
                .content(content)
                .createdBy(createdBy)
                .deleteCheck(deleteCheck)
                .member(member)
                .board(board)
                .build();
    }
}
