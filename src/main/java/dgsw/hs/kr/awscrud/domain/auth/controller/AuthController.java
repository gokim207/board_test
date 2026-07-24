package dgsw.hs.kr.awscrud.domain.auth.controller;

import dgsw.hs.kr.awscrud.domain.auth.dto.AuthMemberResponse;
import dgsw.hs.kr.awscrud.domain.auth.dto.LoginRequest;
import dgsw.hs.kr.awscrud.domain.auth.dto.SignupRequest;
import dgsw.hs.kr.awscrud.domain.auth.service.AuthService;
import dgsw.hs.kr.awscrud.global.security.LoginMember;
import dgsw.hs.kr.awscrud.global.security.SessionConst;
import dgsw.hs.kr.awscrud.global.security.SessionUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthMemberResponse signup(@Valid @RequestBody SignupRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public AuthMemberResponse login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        LoginMember loginMember = authService.login(request);
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return new AuthMemberResponse(loginMember.id(), loginMember.username());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @GetMapping("/me")
    public AuthMemberResponse me(HttpSession session) {
        LoginMember loginMember = SessionUtils.getLoginMember(session);
        return authService.getMember(loginMember.id());
    }
}
