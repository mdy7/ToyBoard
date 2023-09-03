package article.demo.controller;


import article.demo.request.MemberRequestDto;
import article.demo.response.ResponseDto;
import article.demo.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "회원가입")
    @PostMapping
    public ResponseDto<?> createMember(@RequestBody MemberRequestDto memberRequestDto) {
        return memberService.join(memberRequestDto);
    }

    @ApiOperation(value = "회원 전체 조회")
    @GetMapping
    public ResponseDto<?> memberList() {
        return memberService.memberList();
    }

    @ApiOperation(value = "회원수정")
    @PatchMapping
    public ResponseDto<?> updateMember(@RequestBody MemberRequestDto memberRequestDto, HttpSession session){
        String username = (String) session.getAttribute("username");
        return memberService.updateMember(memberRequestDto,username);
    }

    @ApiOperation(value = "회원 삭제")
    @DeleteMapping("/{memberId}")
    public ResponseDto<?> deleteMember(@PathVariable Long memberId,HttpSession session) {
        return memberService.deleteMember(memberId);
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody MemberRequestDto memberRequestDto, HttpSession session){
        ResponseDto<?> responseDto = memberService.login(memberRequestDto);
        session.setAttribute("username", memberRequestDto.getUsername());
        session.setMaxInactiveInterval(1800);  // 세션 유지시간 30분
        return responseDto;
    }

}
