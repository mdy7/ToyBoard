package article.demo.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
/*
기본 생성자(NoArgsConstructor)의 접근 제어를 PROCTECTED 로 설정하면
아무런 값도 갖지 않는 의미 없는 객체의 생성 무분별하게 생성하는 것을 막을 수 있다.
 */
@AllArgsConstructor
@Table(name = "member")
public class Member extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotBlank // validation 을 위한 어노테이션
    @Pattern(regexp = "^[a-zA-Z0-9]{3,12}$", message = "아이디를 3~12자로 입력해주세요. [특수문자 X]")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    //@Pattern(regexp = "^[a-zA-Z0-9]{3,12}$", message = "비밀번호를 3~12자로 입력해주세요.")
    private String password;

    private String email;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Board> board = new ArrayList<>();


}
