package article.demo.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "board_reply")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardReply extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    private String content;
    private String createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private BoardComment  boardComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public BoardReply(String content, String createdBy, BoardComment boardComment, Member member) {
        this.content = content;
        this.createdBy = createdBy;
        this.boardComment = boardComment;
        this.member = member;
    }
}
