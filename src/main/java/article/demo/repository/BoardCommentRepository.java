package article.demo.repository;

import article.demo.domain.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
    List<BoardComment> findByBoardId(Long id);
    }

