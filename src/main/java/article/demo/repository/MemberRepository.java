package article.demo.repository;

import article.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


// findBy Column And Column(String column,String column)
// findBy + column + And(조건) + column + (column,column)
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    List<Member> findAll();

}