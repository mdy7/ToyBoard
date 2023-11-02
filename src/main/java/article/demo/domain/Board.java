package article.demo.domain;


import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "board")
public class Board extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;
    private String content;
    private String createdBy;
    private Long countVisit;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ColumnDefault("0")
    @Column(name = "like_count",nullable = false)
    private Long likeCount;



    public void updateBoard(String title,String content){
        this.title = title;
        this.content = content;
    }

    public void updateVisit(Long countVisit) {
        this.countVisit = countVisit;
    }

    public void updateLike(Long likeCount){
        this.likeCount = likeCount;
    }
}
