package dgsw.hs.kr.awscrud.domain.board.service;

import dgsw.hs.kr.awscrud.domain.auth.entity.Member;
import dgsw.hs.kr.awscrud.domain.auth.repository.MemberRepository;
import dgsw.hs.kr.awscrud.domain.board.dto.BoardCreateRequest;
import dgsw.hs.kr.awscrud.domain.board.dto.BoardResponse;
import dgsw.hs.kr.awscrud.domain.board.dto.BoardUpdateRequest;
import dgsw.hs.kr.awscrud.domain.board.entity.Board;
import dgsw.hs.kr.awscrud.domain.board.repository.BoardRepository;
import dgsw.hs.kr.awscrud.global.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public BoardResponse create(BoardCreateRequest request, MultipartFile image) {
        String imageUrl = s3Uploader.upload(image, "boards");
        Board board = new Board(request.title(), request.content(), imageUrl, null);
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
    public BoardResponse update(Long boardId, Long memberId, BoardUpdateRequest request) {
        Board board = findBoard(boardId);
        validateWriter(board, memberId);
        board.update(request.title(), request.content());
        return BoardResponse.from(board);
    }

    @Transactional
    public void delete(Long boardId, Long memberId) {
        Board board = findBoard(boardId);
        validateWriter(board, memberId);
        boardRepository.delete(board);
    }

    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
    }

    private void validateWriter(Board board, Long memberId) {
        if (!board.getMember().getId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 수정 또는 삭제할 수 있습니다.");
        }
    }
}
