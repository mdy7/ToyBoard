package article.demo.response;

import article.demo.domain.Board;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class MemberResponseDto {
    private Long id;
    private String username;
    private String email;

}