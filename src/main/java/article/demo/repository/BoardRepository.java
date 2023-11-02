package article.demo.repository;

import article.demo.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    default Board findByBoardOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new IllegalStateException("해당 게시글이 존재하지 않습니다."));
    }

    // 게시글 타이틀로 검색 메서드
    List<Board> findByCreatedByOrderByIdDesc(String username); // 작성자로 게시물을 찾아서 id 기준으로 내림차순

    Page<Board> findByTitleContaining(String searchText, Pageable pageable); // 제목으로 검색

    Page<Board> findByContentContaining(String searchText, Pageable pageable); // 내용으로 검색

    Page<Board> findByCreatedByContaining(String searchText, Pageable pageable); // 작성자로 검색


}
