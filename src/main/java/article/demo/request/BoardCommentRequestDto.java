package article.demo.request;

import article.demo.domain.Board;
import article.demo.domain.BoardComment;
import article.demo.domain.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class BoardCommentRequestDto {
    private Long id;
    private String content;
    private String createdBy;
    private Boolean deleteCheck;

    private Member member;
    private Board board;

    private BoardComment parent;

}
