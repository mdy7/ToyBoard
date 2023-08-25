package article.demo.repository;

import article.demo.domain.Board;
import article.demo.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 게시글 수정을 위한 메서드
    @Modifying
    @Query("UPDATE Board b SET b.title = :title, b.content = :content WHERE b.id = :id")
    void updateBoard(@Param("id") Long id, @Param("title") String title, @Param("content") String content);

    // 게시글 타이틀로 검색 메서드
    List<Board> findByCreatedByOrderByIdDesc(String username); // 작성자로 게시물을 찾아서 id 기준으로 내림차순

    Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable); //전체 검색

    Page<Board> findByTitleContaining(String searchText, Pageable pageable); // 제목으로 검색

    Page<Board> findByContentContaining(String searchText, Pageable pageable); // 내용으로 검색

    Page<Board> findByCreatedByContaining(String searchText, Pageable pageable); // 작성자로 검색

}
