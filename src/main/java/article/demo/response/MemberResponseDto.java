package article.demo.response;

import lombok.*;

@Data
@Builder
public class MemberResponseDto {
    private Long id;
    private String username;
    private String email;

}