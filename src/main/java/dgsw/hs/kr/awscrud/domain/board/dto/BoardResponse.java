package dgsw.hs.kr.awscrud.domain.board.dto;

import dgsw.hs.kr.awscrud.domain.board.entity.Board;

import java.time.LocalDateTime;

public record BoardResponse(
        Long id,
        String title,
        String content,
        String writer,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BoardResponse from(Board board) {
        return new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getMember().getUsername(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
}
