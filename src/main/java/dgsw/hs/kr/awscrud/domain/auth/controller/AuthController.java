package dgsw.hs.kr.awscrud.domain.auth.controller;

import dgsw.hs.kr.awscrud.domain.auth.dto.AuthMemberResponse;
import dgsw.hs.kr.awscrud.domain.auth.dto.LoginRequest;
import dgsw.hs.kr.awscrud.domain.auth.dto.LoginResponse;
import dgsw.hs.kr.awscrud.domain.auth.dto.SignupRequest;
import dgsw.hs.kr.awscrud.domain.auth.service.AuthService;
import dgsw.hs.kr.awscrud.global.security.auth.AuthDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
    }

    @GetMapping("/me")
    public AuthMemberResponse me(@AuthenticationPrincipal AuthDetails authDetails) {
        return authService.getMember(authDetails.getMember().getId());
    }
}
