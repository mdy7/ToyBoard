package article.demo.response;


import article.demo.domain.Board;
import article.demo.domain.BoardComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardCommentResponseDto {

    private Long boarId;
    private Long parentId;
    private Long id;
    private String content;
    private String createdBy;


    public static BoardCommentResponseDto toDto(BoardComment boardComment) {
        return BoardCommentResponseDto.builder()
                .boarId(boardComment.getBoard().getId())
                .parentId(boardComment.getParent() == null ? null : boardComment.getParent().getId())
                .id(boardComment.getId())
                .content(boardComment.getContent())
                .createdBy(boardComment.getMember().getUsername())
                .build();
    }
}
