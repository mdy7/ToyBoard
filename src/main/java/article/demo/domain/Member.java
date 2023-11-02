package article.demo.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 의미 없는 객체의 생성 무분별하게 생성하는 것을 막을 수 있음
@AllArgsConstructor
@Table(name = "member")
public class Member extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotNull(message = "아이디를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,12}$", message = "아이디를 3~12자로 입력해주세요.")
    private String username;

    @NotNull(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,12}$", message = "비밀번호를 3~12자로 입력해주세요.")
    private String password;

    private String email;

/*    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Board> boards = new ArrayList<>();*/

    public void update(String password,String email){
        this.password = password;
        this.email = email;
    }
}
