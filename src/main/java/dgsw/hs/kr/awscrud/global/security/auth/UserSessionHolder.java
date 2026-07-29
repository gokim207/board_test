package dgsw.hs.kr.awscrud.global.security.auth;

import dgsw.hs.kr.awscrud.domain.auth.entity.Member;
import dgsw.hs.kr.awscrud.domain.auth.error.AuthErrorCode;
import dgsw.hs.kr.awscrud.domain.auth.repository.MemberRepository;
import dgsw.hs.kr.awscrud.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSessionHolder{
    private final MemberRepository userJpaRepo;

    public Member getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof AuthDetails authDetails) {
            return authDetails.getUser();
        } else {
            throw new CustomException(AuthErrorCode.USER_NOT_FOUND);
        }
    }

    // 편의 메서드: 자주 쓰는 사용자 ID 바로 반환
    public Long getUserId() {
        return getUser().getId();
    }
}
