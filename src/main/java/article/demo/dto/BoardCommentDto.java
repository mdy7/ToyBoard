package article.demo.dto;

import article.demo.domain.Board;
import article.demo.domain.BoardComment;
import article.demo.domain.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class BoardCommentDto {
    private Long id;
    private String content;
    private String createdBy;
    private Boolean deleteCheck;

    private Member member;
    private Board board;

    private BoardComment parent; // 부모댓글 null일 경우 최상위 댓글


    private final List<BoardCommentDto> children = new ArrayList<>();

}
