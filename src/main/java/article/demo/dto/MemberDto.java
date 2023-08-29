package article.demo.dto;

import article.demo.domain.Member;
import lombok.*;

@Data
@Builder
public class MemberDto {

    private Long id;
    private String username;
    private String password;
    private String passwordCheck;
    private String email;


    public Member toEntity(){ // toEntity() 메서드를 통해서 DTO에서 필요한 부분을 이용하여 Entity로 만든다.
        return Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
    }
}
