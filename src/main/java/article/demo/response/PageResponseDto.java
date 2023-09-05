package article.demo.response;


import article.demo.domain.Board;
import lombok.*;

import java.util.List;


@Data
@Builder
public class PageResponseDto {
    private int nowPage;
    private int startPage;
    private int endPage;
    private int totalPages;
    private List<BoardResponseDto> boards;


}
