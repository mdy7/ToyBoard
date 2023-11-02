package article.demo.response;


import article.demo.domain.BoardComment;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
public class BoardCommentResponseDto {

    private Long boarId;
    private Long parentId;
    private Long id;
    private String content;
    private String createdBy;

    private List<BoardComment> children = new ArrayList<>();

    public static BoardCommentResponseDto toDto(BoardComment boardComment) {
        return BoardCommentResponseDto.builder()
                .boarId(boardComment.getBoard().getId())
                .parentId(boardComment.getParent() == null ? null : boardComment.getParent().getId())
                .id(boardComment.getId())
                .content(boardComment.getContent())
                .createdBy(boardComment.getMember().getUsername())
                .children(boardComment.getChildren())
                .build();
    }
}
