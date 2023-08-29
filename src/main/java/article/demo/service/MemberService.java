package article.demo.service;


import article.demo.domain.Member;
import article.demo.dto.MemberDto;
import article.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService{

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(MemberDto memberDto) {
        nullCheckMember(memberDto);

        memberRepository.findByUsername(memberDto.getUsername()).ifPresent( a -> {
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다");
        });

        Member member = Member.builder()
                .username(memberDto.getUsername())
                .password(memberDto.getPassword())
                .email(memberDto.getEmail())
                .build();

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    /**
     * 로그인
     */
    public void login(MemberDto memberDto){
        nullCheckMember(memberDto);

        Member member = memberRepository.getUsername(memberDto.getUsername());

        if (!memberDto.getPassword().equals(member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }


    /**
     * 회원 수정
     */

    @Transactional
    public Member updateMember(MemberDto memberDto,String username){
        Member member = memberRepository.getUsername(username);

        if(!memberDto.getPassword().equals(memberDto.getPasswordCheck())){
            throw new IllegalStateException("비밀번호를 다시 입력해주세요");
        }

        member.update(memberDto.getPassword(), memberDto.getEmail());

        return memberRepository.save(member);
    }

    public Member getUsernameForm(String username){
        return memberRepository.getUsername(username);
    }

    /**
     * 검증
     */
    private void nullCheckMember(MemberDto memberDto) {
        if (memberDto.getUsername() == null || memberDto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("아이디를 입력해주세요");
        }
        if (memberDto.getPassword() == null || memberDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요");
        }
    }
}

