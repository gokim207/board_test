package dgsw.hs.kr.awscrud.domain.board.service;

import dgsw.hs.kr.awscrud.domain.auth.entity.Member;
import dgsw.hs.kr.awscrud.domain.auth.repository.MemberRepository;
import dgsw.hs.kr.awscrud.domain.board.dto.BoardCreateRequest;
import dgsw.hs.kr.awscrud.domain.board.dto.BoardResponse;
import dgsw.hs.kr.awscrud.domain.board.dto.BoardUpdateRequest;
import dgsw.hs.kr.awscrud.domain.board.entity.Board;
import dgsw.hs.kr.awscrud.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BoardResponse create(Long memberId, BoardCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."));

        Board board = new Board(request.title(), request.content(), member);
        return BoardResponse.from(boardRepository.save(board));
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> getAll() {
        return boardRepository.findAll().stream()
                .map(BoardResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public BoardResponse getById(Long boardId) {
        return BoardResponse.from(findBoard(boardId));
    }

    @Transactional
    public BoardResponse update(Long boardId, BoardUpdateRequest request) {
        Board board = findBoard(boardId);
        board.update(request.title(), request.content());
        return BoardResponse.from(board);
    }

    @Transactional
    public void delete(Long boardId) {
        boardRepository.delete(findBoard(boardId));
    }

    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
    }
}
