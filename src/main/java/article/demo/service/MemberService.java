package article.demo.service;


import article.demo.domain.Member;
import article.demo.request.RequestMemberDto;
import article.demo.repository.MemberRepository;
import article.demo.response.MemberResponseDto;
import article.demo.response.ResponseDto;
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
    public ResponseDto<?> join(RequestMemberDto requestMemberDto) {
        nullCheckMemberForm(requestMemberDto);

        memberRepository.findByUsername(requestMemberDto.getUsername()).ifPresent(a -> {
            throw new IllegalArgumentException("이미 사용중인 아이디 입니다");
        });

        memberRepository.findByEmail(requestMemberDto.getEmail()).ifPresent(a -> {
            throw new IllegalArgumentException("이미 사용중인 이메일 입니다");
        });

        Member member = Member.builder()
                .username(requestMemberDto.getUsername())
                .password(requestMemberDto.getPassword())
                .email(requestMemberDto.getEmail())
                .build();

        memberRepository.save(member);

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .username(member.getUsername())
                        .build()
        );
    }

    private void validateDuplicateUsernameAndEmail(String username, String email) {
        memberRepository.findByUsername(username).ifPresent(a -> {
            throw new IllegalArgumentException("이미 사용중인 아이디 입니다");
        });

        memberRepository.findByEmail(email).ifPresent(a -> {
            throw new IllegalArgumentException("이미 사용중인 이메일 입니다");
        });
    }

    /**
     * 로그인
     */
    public ResponseDto<?> login(RequestMemberDto requestMemberDto){
        nullCheckMemberForm(requestMemberDto);

        Member member = memberRepository.findByUsername(requestMemberDto.getUsername()).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 아이디 입니다"));

        if (!requestMemberDto.getPassword().equals(member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .username(member.getUsername())
                        .build()
        );
    }

    /**
     * 회원 수정
     */
    @Transactional
    public ResponseDto<?> updateMember(RequestMemberDto requestMemberDto, String username){
        Member member = memberRepository.getMemberByUsername(username);

        log.info("Password = {}", requestMemberDto.getPassword());
        log.info("PasswordConfirm = {}", requestMemberDto.getPasswordConfirm());

        if(!requestMemberDto.getPassword().equals(requestMemberDto.getPasswordConfirm())){
            throw new IllegalStateException("비밀번호를 다시 입력해주세요");
        }

        member.update(requestMemberDto.getPassword(), requestMemberDto.getEmail());

        memberRepository.save(member);

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .username(member.getUsername())
                        .build()
        );
    }

    public Member getUsernameForm(String username){
        return memberRepository.getMemberByUsername(username);
    }

    /**
     * 검증
     */
    private void nullCheckMemberForm(RequestMemberDto requestMemberDto) {
        if (requestMemberDto.getUsername() == null || requestMemberDto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("아이디를 입력해주세요");
        }
        if (requestMemberDto.getPassword() == null || requestMemberDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요");
        }
    }
}

