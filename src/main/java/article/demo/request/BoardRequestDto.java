package article.demo.request;

import article.demo.domain.Member;
import lombok.*;

@Data
@Builder
public class BoardRequestDto {

    private String title;
    private String content;
    private String createdBy;
    private Long countVisit;
    private Long likeCount;
    private Member member;

    public void updateCreateBy(String createdBy, Long countVisit,Long likeCount,Member member) {
        this.createdBy = createdBy;
        this.countVisit = countVisit;
        this.likeCount = likeCount;
        this.member = member;
    }

}
