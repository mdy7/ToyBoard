package article.demo.controller;

import article.demo.domain.Member;
import article.demo.dto.MemberDto;
import article.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/JoinForm")
    public String createMemberForm(){
        return "member/memberJoinForm";
    }

    @PostMapping("/JoinForm")
    public String createMember(MemberDto memberDto){
        memberService.join(memberDto);
        return "redirect:/";
    }

    @GetMapping("/LoginForm")
    public String loginForm(){
        return "member/memberLoginForm";
    }

    @PostMapping("/LoginForm")
    public String login(HttpSession session,MemberDto memberDto){
        memberService.login(memberDto);
        session.setAttribute("username",memberDto.getUsername());
        session.setMaxInactiveInterval(1800);  // 세션 유지시간 30분
        return "redirect:/";
    }

    @GetMapping("/UpdateForm")
    public String updateForm(HttpSession session, Model model){
        String username = (String) session.getAttribute("username");
        Member member = memberService.getUsernameForm(username);

        model.addAttribute("member",member);
        return "member/memberUpdateForm";
    }

    @PostMapping("/UpdateForm")
    public String updateMember(MemberDto memberDto,HttpSession session){
        String username = (String) session.getAttribute("username");
        memberService.updateMember(memberDto,username);
        return "redirect:/";
    }

    @GetMapping("/Logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}
