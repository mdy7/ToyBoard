package article.demo.response;


import article.demo.domain.Board;
import article.demo.domain.BoardComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardCommentResponseDto {

    private Long id;
    private String content;
    private String createdBy;

    public static BoardCommentResponseDto toDto(BoardComment boardComment) {
        return new BoardCommentResponseDto(
                boardComment.getId(),
                boardComment.getContent(),
                boardComment.getMember().getUsername()
        );
    }
}
