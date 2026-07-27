package dgsw.hs.kr.awscrud.global.security.jwt;

import dgsw.hs.kr.awscrud.global.security.jwt.util.JwtExtractor;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final List<String> WHITE_LIST = List.of(
            "/health",
            "/api/auth/signup",
            "/api/auth/login",
            "/api/board/**",
            "/swagger-ui",
            "/swagger-ui.html",
            "/api-docs",
            "/v3/api-docs",
            "/h2-console"
    );

    private final JwtExtractor jwtExtractor;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        return WHITE_LIST.stream().anyMatch(requestUri::startsWith);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = jwtExtractor.resolveToken(request);
            if (token != null) {
                SecurityContextHolder.getContext().setAuthentication(jwtExtractor.getAuthentication(token));
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException exception) {
            writeUnauthorized(response, "토큰이 만료되었습니다.");
        } catch (JwtException | IllegalArgumentException exception) {
            writeUnauthorized(response, "유효하지 않은 토큰입니다.");
        }
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"message\":\"" + message + "\"}");
    }
}
