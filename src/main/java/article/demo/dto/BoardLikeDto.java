package article.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardLikeDto {

    private Long id;

    private Long memberId;
    private Long boardId;

}