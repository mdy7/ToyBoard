package article.demo.response;

import article.demo.domain.Board;
import article.demo.domain.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardResponseDto {
    private Long id;
    private String createBy;
    private String title;
    private String content;
    private Long countVisit;
    private Long likeCount;
    private LocalDateTime createdDate;

    public static BoardResponseDto toDto(Board board) {
        return new BoardResponseDto(
                board.getId(),
                board.getMember().getUsername(),
                board.getTitle(),
                board.getContent(),
                board.getCountVisit(),
                board.getLikeCount(),
                board.getCreatedDate()
                );
    }
}
