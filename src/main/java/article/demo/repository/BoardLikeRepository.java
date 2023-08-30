package article.demo.repository;

import article.demo.domain.Board;
import article.demo.domain.BoardLike;
import article.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    Optional<Object> findByMemberAndBoard(Member member, Board board);
}
