package article.demo.repository;

import article.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getMemberByUsername(String username) {
        return findByUsername(username).orElseThrow(() ->
                new IllegalStateException("로그인이 필요 합니다"));
    }


    Optional<Member> findByUsername(String username);

    List<Member> findAll();

    Optional<Member> findByEmail(String email);

}