package article.demo.repository;

import article.demo.domain.BoardComment;
import article.demo.domain.BoardCommentReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentReplyRepository extends JpaRepository<BoardCommentReply, Long> {

}
