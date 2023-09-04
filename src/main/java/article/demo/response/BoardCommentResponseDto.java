package article.demo.response;


import article.demo.domain.BoardComment;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
public class BoardCommentResponseDto {

    private Long boarId;
    private Long parentId;
    private Long id;
    private String content;
    private String createdBy;
    //private List<BoardComment> children;


    public static BoardCommentResponseDto toDto(BoardComment boardComment) {
        return BoardCommentResponseDto.builder()
                .boarId(boardComment.getBoard().getId())
                .parentId(boardComment.getParent() == null ? null : boardComment.getParent().getId())
                .id(boardComment.getId())
                .content(boardComment.getContent())
                .createdBy(boardComment.getMember().getUsername())
                //.children(boardComment.getChildren())
                .build();
    }

    @Getter
    @Setter
    public static class Children {
        private Long id;
        private Long parentId;
        private String content;
        private String createdBy;
    }
}
