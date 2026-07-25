package dgsw.hs.kr.awscrud.domain.auth.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        Long memberId,
        String username
) {
}
