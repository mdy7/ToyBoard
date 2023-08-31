package article.demo.controller;

import article.demo.domain.Member;
import article.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberGetController {

    private final MemberService memberService;

    @GetMapping("/joinForm")
    public String createMemberForm(){
        return "member/memberJoinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "member/memberLoginForm";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/updateForm")
    public String updateForm(HttpSession session, Model model){
        String username = (String) session.getAttribute("username");
        Member member = memberService.getUsernameForm(username);

        model.addAttribute("member",member);
        return "member/memberUpdateForm";
    }
}
