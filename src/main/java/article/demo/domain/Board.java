package article.demo.domain;


import lombok.*;

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
    @JoinColumn(name = "member_id") // 외래키 설정
    private Member member;


    public void updateBoard(String title,String content){
        this.title = title;
        this.content = content;
    }

    public void updateVisit(Long countVisit) {
        this.countVisit = countVisit;
    }
}
