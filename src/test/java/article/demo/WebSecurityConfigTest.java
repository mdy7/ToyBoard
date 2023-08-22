package article.demo;

import article.demo.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class WebSecurityConfigTest {

    WebSecurityConfig wc = new WebSecurityConfig();

    @Test
    @DisplayName("패스워드 암호화 테스트")
    void encodeTest() {
        // given
        String rawPW = "1234";

        // when
        String encodePW = wc.passwordEncoder().encode(rawPW);


        // then
        assertThat(rawPW).isNotEqualTo(encodePW);
    }

    @Test
    @DisplayName("패스워드 불일치 테스트")
    void notMatchTest() {
        // given
        String originalPW = "1234"; // 설정한 PW의 원본
        String inputPW = "123456"; // 입력한 PW의 원본
        String encodePW = wc.passwordEncoder().encode(originalPW); // 설정한 PW를 인코딩한 값

        // when
        Boolean check = wc.passwordEncoder().matches(inputPW, encodePW);
        // 입력한 PW와 설정한 PW를 인코딩한 값을 matches()를 통해 비교한다.
        // originalPW와 inputPW가 다르기 때문에 false 나와야 함.

        // then
        assertThat(check).isEqualTo(false);
    }

}