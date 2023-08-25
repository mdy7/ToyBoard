package article.demo.controller;

import article.demo.dto.MemberDto;
import article.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/memberJoinForm")
    public String createMemberForm(){
        return "member/memberJoinForm";
    }

    @PostMapping("/memberJoinForm")
    public String createMember(MemberDto memberDto){
        memberService.join(memberDto);
        return "redirect:/";
    }

    @GetMapping("/memberLoginForm")
    public String loginForm(){
        return "member/memberLoginForm";
    }

    @PostMapping("/memberLoginForm")
    public String login(HttpSession session,MemberDto memberDto){
        memberService.login(memberDto);
        session.setAttribute("username",memberDto.getUsername());
        session.setMaxInactiveInterval(1800);  // 세션 유지시간 30분
        return "redirect:/";
    }

    @GetMapping("/memberLogout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}
