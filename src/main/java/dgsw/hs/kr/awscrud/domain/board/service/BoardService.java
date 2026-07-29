package dgsw.hs.kr.awscrud.domain.board.service;

import dgsw.hs.kr.awscrud.domain.auth.entity.Member;
import dgsw.hs.kr.awscrud.domain.auth.repository.MemberRepository;
import dgsw.hs.kr.awscrud.global.security.auth.UserSessionHolder;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import dgsw.hs.kr.awscrud.domain.board.dto.BoardCreateRequest;
import dgsw.hs.kr.awscrud.domain.board.dto.BoardResponse;
import dgsw.hs.kr.awscrud.domain.board.dto.BoardUpdateRequest;
import dgsw.hs.kr.awscrud.domain.board.entity.Board;
import dgsw.hs.kr.awscrud.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final UserSessionHolder userSessionHolder;
    private final S3Operations s3Operations;


    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public BoardResponse create(BoardCreateRequest request, MultipartFile image) {
        String originFilename = image.getOriginalFilename();
        String substring = originFilename.substring(originFilename.lastIndexOf(".") + 1);

        String fileName = Instant.now().getEpochSecond() + substring;
        String imageUrl = "";

        try {
            InputStream inputStream = image.getInputStream();
            S3Resource upload = s3Operations.upload(bucket, fileName, inputStream,
                    ObjectMetadata.builder()
                            .contentType(image.getContentType())
                            .build());
            imageUrl = upload.getURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Member sessionUser = userSessionHolder.getUser();
        Member member = memberRepository.findByUsername(sessionUser.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Board board = new Board(request.title(), request.content(), imageUrl, member);
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
