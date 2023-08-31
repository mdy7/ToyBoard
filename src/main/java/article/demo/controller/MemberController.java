package article.demo.controller;

import article.demo.domain.Member;
import article.demo.request.RequestMemberDto;
import article.demo.response.ResponseDto;
import article.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/joinForm")
    public ResponseDto<?> createMember(@RequestBody RequestMemberDto requestMemberDto) {
        return memberService.join(requestMemberDto);
    }

    @PostMapping("/loginForm")
    public ResponseDto<?> login(@RequestBody RequestMemberDto requestMemberDto, HttpSession session){
        ResponseDto<?> responseDto = memberService.login(requestMemberDto);
        session.setAttribute("username", requestMemberDto.getUsername());
        session.setMaxInactiveInterval(1800);  // 세션 유지시간 30분
        return responseDto;
    }

    @PostMapping("/updateForm")
    public ResponseDto<?> updateMember(@RequestBody RequestMemberDto requestMemberDto, HttpSession session){
        String username = (String) session.getAttribute("username");
        return memberService.updateMember(requestMemberDto,username);
    }
}
