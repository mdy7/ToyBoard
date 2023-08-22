package article.demo.service;


import article.demo.domain.Member;
import article.demo.dto.MemberDto;
import article.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService{

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(MemberDto memberDto) {
        validateNullCheckMember(memberDto);
        validateDuplicateMember(memberDto);
        Member savedMember = memberRepository.save(memberDto.toEntity());
        /*
            save() 내부 구현에서는 파라미터로 들어온 entity 가 새로운 엔티티라면 persist 를 호출하고
            아니라면 merge(update 와 비슷)를 한다.
            save() 메소드를 통해 단순히 저장하려고 했지만 이미 동일한 데이터가 있는경우 update 쿼리가 발생한다
         */
        return savedMember.getId();
    }

    public void login(MemberDto memberDto){
        validateNullCheckMember(memberDto);
        validationLogin(memberDto);
    }


    private void validateNullCheckMember(MemberDto memberDto) {
        if (memberDto.getUsername() == null || memberDto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("아이디를 입력해주세요");
        }

        if (memberDto.getPassword() == null || memberDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요");
        }
    }

    private void validateDuplicateMember(MemberDto memberDto) {
        Optional<Member> findMembers = memberRepository.findByUsername(memberDto.getUsername());
        if (findMembers.isPresent()) { //isPresent() : Optional 객체가 값을 가지고 있다면 true, 값이 없다면 false 리턴
            throw new IllegalStateException("이미 존재하는 아이디 입니다");
        }
    }
    private void validationLogin(MemberDto memberDto) {
        Optional<Member> findMembers = memberRepository.findByUsername(memberDto.getUsername());

        findMembers.orElseThrow(() -> new IllegalStateException("존재하지 않는 아이디 입니다"));
        Member member = findMembers.get();
        if (!memberDto.getPassword().equals(member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}

//    @Override
//    public UserDetails loadUserByUsername(String username) {
//        Optional<Member> findName = memberRepository.findByUsername(username);
//        Member member = findName.get();
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        if(("admin").equals(username)){ // 권한 부여
//            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
//        }else {
//            authorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));
//        }
//        return new User(member.getUsername(), member.getPassword(), authorities);
//    }
