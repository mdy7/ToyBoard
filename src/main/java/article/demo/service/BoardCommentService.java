package article.demo.service;


import article.demo.domain.Board;
import article.demo.domain.BoardComment;
import article.demo.domain.Member;
import article.demo.dto.BoardCommentDto;
import article.demo.repository.BoardCommentRepository;
import article.demo.repository.BoardRepository;
import article.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardService boardService;

    @Transactional
    public void saveBoardComment(Long id, BoardCommentDto boardCommentDto,String username) {
        Board board = boardRepository.findById(id).orElseThrow((() ->
                new IllegalStateException("해당 게시글이 존재하지 않습니다")));

        Member member = memberRepository.findByUsername(username).orElseThrow((() ->
                new IllegalStateException("존재하지 않는 아이디 입니다")));

        BoardComment boardComment = BoardComment.builder()
                .createdBy(username)
                .createdData(LocalDateTime.now())
                .deleteCheck('N')
                .member(member)
                .board(board)
                .content(boardCommentDto.getContent())
                .build();

        boardCommentRepository.save(boardComment);
    }


    public List<BoardComment> findCommentBoardId(Long id) {
        List<BoardComment> comments = boardCommentRepository.findCommentBoardId(id);
        return comments;
    }
}
