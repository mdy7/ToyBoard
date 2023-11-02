package article.demo.request;


import article.demo.domain.BoardComment;
import lombok.*;


@Data
@Builder
public class BoardCommentRequestDto {
    private String content;
    private BoardComment parent;

}
