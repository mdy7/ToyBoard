package article.demo.repository;

import article.demo.domain.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

/*    @Query("SELECT c from BoardComment c where c.board.id= :id")
    List<BoardComment> findCommentBoardId(@Param("id") Long id);*/

    List<BoardComment> findByBoardId(Long id);

}
