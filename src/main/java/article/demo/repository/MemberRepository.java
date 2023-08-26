package article.demo.repository;

import article.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getUsername(String username) {
        return findByUsername(username).orElseThrow(() -> new IllegalStateException("존재하지 않는 아이디 입니다."));
    }

    default Member getUsernameBySession(String username) {
        return findByUsername(username).orElseThrow(() ->  new IllegalStateException("로그인 정보가 없습니다."));
    }

    Optional<Member> findByUsername(String username);
    List<Member> findAll();

}