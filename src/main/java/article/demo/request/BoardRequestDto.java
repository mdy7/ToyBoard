package article.demo.request;

import article.demo.domain.Board;
import article.demo.domain.Member;
import lombok.*;

@Data
@Builder
public class BoardRequestDto {

    private Long id;
    private String title;
    private String content;
    private String createdBy;
    private Long countVisit;
    private Long likeCount;
    private Member member;

    public Board toEntity(){
        return Board.builder()
                .title(title)
                .content(content)
                .createdBy(createdBy)
                .countVisit(countVisit)
                .member(member)
                .build();
    }

    public void updateCreateBy(String createdBy, Long countVisit,Long likeCount,Member member) {
        this.createdBy = createdBy;
        this.countVisit = countVisit;
        this.likeCount = likeCount;
        this.member = member;
    }

}
