package article.demo;

import article.demo.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@EnableWebSecurity
//@AllArgsConstructor
public class WebSecurityConfig {

//    private final MemberService memberService;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/member/myinfo").hasRole("USER")
//                .antMatchers("/**").permitAll()
//                .and()
//                .cors().disable()
//                .csrf().disable()
//                .formLogin()
//                //.authorizeRequests()             // 요청 권한 설정 시작
//                .loginPage("/member/memberLogin")
//                .defaultSuccessUrl("/member/checkSession")
//                .and()                           // 권한 설정 종료
//                .logout()
//                    .logoutUrl("/member/logout") // 로그아웃 URL 지정
//                    .logoutSuccessUrl("/member/checkSession")  // 로그아웃 성공 후 이동할 URL 지정
//                    .invalidateHttpSession(true) // 세션 무효화 여부 설정 (로그아웃 후 세션 제거)
//                    .clearAuthentication(true)   // 인증 정보 제거 여부 설정 (로그아웃 후 인증 정보 제거)
//                    .permitAll();                // 로그아웃 URL은 모든 사용자 접근 가능
//    }
//
//    // 모든 인증을 처리하기 위한 AuthenticationManager
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
//    }

}
