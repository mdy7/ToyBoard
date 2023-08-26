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

        Member savedMember = memberRepository.save(memberDto.toEntity());
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

