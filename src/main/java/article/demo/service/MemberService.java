package article.demo.service;


import article.demo.domain.Board;
import article.demo.domain.Member;
import article.demo.request.MemberRequestDto;
import article.demo.repository.MemberRepository;
import article.demo.response.BoardResponseDto;
import article.demo.response.MemberResponseDto;
import article.demo.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional
    public ResponseDto<?> join(MemberRequestDto memberRequestDto) {
        if (null != isPresentUsername(memberRequestDto.getUsername())) {
            return ResponseDto.fail("DUPLICATED_USERNAME", "이미 사용중인 아이디 입니다.");
        }

        if (null != isPresentEmail(memberRequestDto.getEmail())) {
            return ResponseDto.fail("DUPLICATED_EMAIL", "이미 사용중인 이메일 입니다.");
        }

        Member member = Member.builder()
                .username(memberRequestDto.getUsername())
                .password(memberRequestDto.getPassword())
                .email(memberRequestDto.getEmail())
                .build();

        memberRepository.save(member);

        return ResponseDto.success(
                "회원가입 성공",
                MemberResponseDto.builder()
                        .id(member.getId())
                        .username(member.getUsername())
                        .build()
        );
    }

    public Member isPresentUsername(String username) {
        Optional<Member> findUsername = memberRepository.findByUsername(username);
        return findUsername.orElse(null);
    }

    public Member isPresentEmail(String email) {
        Optional<Member> findEmail = memberRepository.findByEmail(email);
        return findEmail.orElse(null);
    }

    /**
     * 로그인
     */
    public ResponseDto<?> login(MemberRequestDto memberRequestDto) {
        Member member = isPresentUsername(memberRequestDto.getUsername());

        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUNT", "존재하지 않는 아이디 입니다.");
        }

        if (!memberRequestDto.getPassword().equals(member.getPassword())) {
            return ResponseDto.fail("INVALID_MEMBER", "패스워드가 일치하지 않습니다.");
        }

        return ResponseDto.success(
                "로그인 성공",
                MemberResponseDto.builder()
                        .id(member.getId())
                        .username(member.getUsername())
                        .build()
        );
    }

    /**
     * 회원 전체 조회
     */
    public ResponseDto<?> memberList() {
        List<Member> members = memberRepository.findAll();
        List<MemberResponseDto> memberResponseDto = new ArrayList<>();

        for (Member member : members) {
            MemberResponseDto dto = MemberResponseDto.builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .email(member.getEmail())
                    .build();
            memberResponseDto.add(dto);
        }
        return ResponseDto.success("전체 회원 조회", memberResponseDto);
    }


    /**
     * 회원 수정
     */
    @Transactional
    public ResponseDto<?> updateMember(MemberRequestDto memberRequestDto, String username) {
        Member member = memberRepository.getMemberByUsername(username);

        if (!memberRequestDto.getPassword().equals(memberRequestDto.getPasswordConfirm())) {
            return ResponseDto.fail("PASSWORDS_NOT_MATCHED", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        member.update(memberRequestDto.getPassword(), memberRequestDto.getEmail());

        memberRepository.save(member);

        return ResponseDto.success(
                "회원 수정 성공",
                MemberResponseDto.builder()
                        .id(member.getId())
                        .username(member.getUsername())
                        .build()
        );
    }


    /**
     * 회원 삭제
     */
    @Transactional
    public ResponseDto<?> deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalStateException("없는 아이디 입니다"));

        memberRepository.delete(member);

        return ResponseDto.success("회원 삭제 성공",null);
    }


    public Member getUsernameForm(String username) {
        return memberRepository.getMemberByUsername(username);
    }

    /**
     * 검증
     */
    private ResponseDto<?> nullCheckMemberForm(MemberRequestDto memberRequestDto) {
        if (memberRequestDto.getUsername() == null || memberRequestDto.getUsername().trim().isEmpty()) {
//            return ResponseDto.fail("NULL_ID", "아이디를 입력해 주세요");
            throw new IllegalStateException("아이디를 입력해 주세요");

        }
        if (memberRequestDto.getPassword() == null || memberRequestDto.getPassword().trim().isEmpty()) {
//            return ResponseDto.fail("NULL_PASSWORD", "패스워드를 입력해 주세요");
            throw new IllegalStateException("패스워드를 입력해 주세요");

        }
        return null;
    }


}
