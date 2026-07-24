package dgsw.hs.kr.awscrud.domain.board.controller;

import dgsw.hs.kr.awscrud.domain.board.dto.BoardCreateRequest;
import dgsw.hs.kr.awscrud.domain.board.dto.BoardResponse;
import dgsw.hs.kr.awscrud.domain.board.dto.BoardUpdateRequest;
import dgsw.hs.kr.awscrud.domain.board.service.BoardService;
import dgsw.hs.kr.awscrud.global.security.LoginMember;
import dgsw.hs.kr.awscrud.global.security.SessionUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoardResponse createBoard(
            @Valid @RequestBody BoardCreateRequest request,
            HttpSession session
    ) {
        LoginMember loginMember = SessionUtils.getLoginMember(session);
        return boardService.create(loginMember.id(), request);
    }

    @GetMapping
    public List<BoardResponse> getBoards() {
        return boardService.getAll();
    }

    @GetMapping("/{boardId}")
    public BoardResponse getBoard(@PathVariable Long boardId) {
        return boardService.getById(boardId);
    }

    @PutMapping("/{boardId}")
    public BoardResponse updateBoard(
            @PathVariable Long boardId,
            @Valid @RequestBody BoardUpdateRequest request
    ) {
        return boardService.update(boardId, request);
    }

    @DeleteMapping("/{boardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBoard(@PathVariable Long boardId) {
        boardService.delete(boardId);
    }
}
