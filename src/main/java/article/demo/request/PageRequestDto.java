package article.demo.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageRequestDto {
    private int page; // 현재 페이지
    private int size; // 한개 페이지안에 게시판 수
}
