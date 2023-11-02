package article.demo.request;

import article.demo.domain.Member;
import lombok.*;

@Data
@Builder
public class MemberRequestDto {

    private String username;
    private String password;
    private String passwordConfirm;
    private String email;


    public Member toEntity(){ // toEntity() 메서드를 통해서 DTO에서 필요한 부분을 이용하여 Entity로 만든다.
        return Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
    }
}
