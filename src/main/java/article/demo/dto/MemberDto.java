package article.demo.dto;

import article.demo.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String username;
    private String password;
    private String email;

    @Builder
    public MemberDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Member toEntity(){ // toEntity() 메서드를 통해서 DTO에서 필요한 부분을 이용하여 Entity로 만든다.
        return Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();

    }

    public void encodePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
