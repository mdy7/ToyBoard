package article.demo.dto;

import article.demo.domain.Board;
import lombok.*;

@Data
@Builder
public class BoardDto {

    private Long id;
    private String title;
    private String content;
    private String createdBy;
    private Long countVisit;

    public Board toEntity(){
        return Board.builder()
                .title(title)
                .content(content)
                .createdBy(createdBy)
                .countVisit(countVisit)
                .build();
    }

    public void updateCreateBy(String createdBy, Long countVisit) {
        this.createdBy = createdBy;
        this.countVisit = countVisit;
    }

}
