package article.demo.dto;

import article.demo.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardDto {

    private Long id;
    private String title;
    private String content;
    private String createdBy;
    private Long countVisit;

    @Builder
    public BoardDto(String title, String content, String createdBy,Long countVisit) {
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
        this.countVisit = countVisit;
    }

    public Board toEntity(){
        return Board.builder()
                .title(title)
                .content(content)
                .createdBy(createdBy)
                .countVisit(countVisit)
                .build();
    }

    public BoardDto(Board board) {
        id = board.getId();
        title = board.getTitle();
        content = board.getContent();
        createdBy = board.getCreatedBy();
        countVisit = board.getCountVisit();
    }

    public void boardSetting(String createdBy, Long countVisit) {
        this.createdBy = createdBy;
        this.countVisit = countVisit;
    }

}
