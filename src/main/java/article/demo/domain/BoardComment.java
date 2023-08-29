package article.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@Table(name = "board_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardComment extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_comment_id")
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    private String createdBy;
    private Boolean deleteCheck;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private BoardComment parent; // 부모댓글 null일 경우 최상위 댓글

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<BoardComment> children = new ArrayList<>(); // 자식댓글

    public void updateDelete(Boolean deleteCheck) {
        this.deleteCheck = deleteCheck;
    }
}
