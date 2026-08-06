package dgsw.hs.kr.awscrud.domain.board.controller;

import dgsw.hs.kr.awscrud.domain.board.dto.BoardCreateRequest;
import dgsw.hs.kr.awscrud.domain.board.dto.BoardResponse;
import dgsw.hs.kr.awscrud.domain.board.dto.BoardUpdateRequest;
import dgsw.hs.kr.awscrud.domain.board.service.BoardService;
import dgsw.hs.kr.awscrud.global.security.auth.AuthDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public BoardResponse createBoard(
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return boardService.create(new BoardCreateRequest(title, content), image);
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
            @Valid @RequestBody BoardUpdateRequest request,
            @AuthenticationPrincipal AuthDetails authDetails
    ) {
        return boardService.update(boardId, authDetails.getMember().getId(), request);
    }

    @DeleteMapping("/{boardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal AuthDetails authDetails
    ) {
        boardService.delete(boardId, authDetails.getMember().getId());
    }
}
