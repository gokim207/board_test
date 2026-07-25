package dgsw.hs.kr.awscrud.global.security.jwt.util;

import dgsw.hs.kr.awscrud.domain.auth.entity.Member;
import dgsw.hs.kr.awscrud.domain.auth.repository.MemberRepository;
import dgsw.hs.kr.awscrud.global.security.auth.AuthDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class JwtExtractor {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);

        if (bearerToken == null || !bearerToken.startsWith(BEARER_PREFIX)) {
            return null;
        }

        return bearerToken.substring(BEARER_PREFIX.length());
    }

    public Authentication getAuthentication(String token) {
        Long memberId = jwtProvider.getMemberId(token);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "존재하지 않는 사용자입니다."));

        AuthDetails authDetails = new AuthDetails(member);
        return new UsernamePasswordAuthenticationToken(authDetails, null, authDetails.getAuthorities());
    }
}
