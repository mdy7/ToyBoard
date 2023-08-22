package article.demo.service;

import article.demo.domain.Member;
import article.demo.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Commit
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

/*
    @Test
    void 회원가입() {
        //given 주어졌을때
        Member member = Member.builder()
                .username("kim")
                .password("123")
                .email("email")
                .build();
        
        //when 실행하면
        Long joinId = memberService.join(member);

        //then 결과
        Assertions.assertEquals(member, memberRepository.findOne(joinId));

    }

    @Test
    void 중복_회원_가입() {
        //given
        Member member1 = Member.builder()
                .username("kim")
                .password("123")
                .email("email")
                .build();

        Member member2 = Member.builder()
                .username("kim")
                .password("123")
                .email("email")
                .build();

        //when
        memberService.join(member1);

        //then
        assertThrows(IllegalStateException.class , () -> memberService.join(member2));

    }
*/

}