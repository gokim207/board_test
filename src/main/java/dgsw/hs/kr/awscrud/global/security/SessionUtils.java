package dgsw.hs.kr.awscrud.global.security;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class SessionUtils {

    private SessionUtils() {
    }

    public static LoginMember getLoginMember(HttpSession session) {
        Object value = session.getAttribute(SessionConst.LOGIN_MEMBER);

        if (!(value instanceof LoginMember loginMember)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        return loginMember;
    }
}
