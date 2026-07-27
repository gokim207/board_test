package dgsw.hs.kr.awscrud.domain.board.dto;

import dgsw.hs.kr.awscrud.domain.board.entity.Board;

import java.time.LocalDateTime;

public record BoardResponse(
        Long id,
        String title,
        String content,
        String imageUrl,
        String writer,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BoardResponse from(Board board) {
        return new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getImageUrl(),
                board.getMember() != null ? board.getMember().getUsername() : "anonymous",
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
}
