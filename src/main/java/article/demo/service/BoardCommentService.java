package article.demo.service;


import article.demo.domain.Board;
import article.demo.domain.BoardComment;
import article.demo.domain.Member;
import article.demo.dto.BoardCommentDto;
import article.demo.dto.BoardDto;
import article.demo.repository.BoardCommentRepository;
import article.demo.repository.BoardRepository;
import article.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveBoardComment(Long id, BoardCommentDto boardCommentDto,String username) {
        if (boardCommentDto.getContent() == null || boardCommentDto.getContent().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요");
        }

        Board board = boardRepository.getBoard(id);

        Member member = memberRepository.findByUsername(username).orElseThrow((() ->
                new IllegalStateException("로그인 정보가 없습니다")));

        BoardComment boardComment = BoardComment.builder()
                .createdBy(username)
                .deleteCheck('N')
                .member(member)
                .board(board)
                .content(boardCommentDto.getContent())
                .build();

        boardCommentRepository.save(boardComment);
    }

    public List<BoardComment> findCommentBoardId(Long id) {
        return boardCommentRepository.findByBoardId(id);
    }

}
