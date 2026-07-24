package dgsw.hs.kr.awscrud.domain.auth.service;

import dgsw.hs.kr.awscrud.domain.auth.dto.AuthMemberResponse;
import dgsw.hs.kr.awscrud.domain.auth.dto.LoginRequest;
import dgsw.hs.kr.awscrud.domain.auth.dto.SignupRequest;
import dgsw.hs.kr.awscrud.domain.auth.entity.Member;
import dgsw.hs.kr.awscrud.domain.auth.repository.MemberRepository;
import dgsw.hs.kr.awscrud.global.security.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthMemberResponse signup(SignupRequest request) {
        if (memberRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다.");
        }

        Member member = new Member(
                request.username(),
                passwordEncoder.encode(request.password())
        );

        Member savedMember = memberRepository.save(member);
        return new AuthMemberResponse(savedMember.getId(), savedMember.getUsername());
    }

    @Transactional(readOnly = true)
    public LoginMember login(LoginRequest request) {
        Member member = memberRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return new LoginMember(member.getId(), member.getUsername());
    }

    @Transactional(readOnly = true)
    public AuthMemberResponse getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."));

        return new AuthMemberResponse(member.getId(), member.getUsername());
    }
}
