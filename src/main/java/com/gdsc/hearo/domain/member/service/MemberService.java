package com.gdsc.hearo.domain.member.service;

import com.gdsc.hearo.domain.member.dto.LoginRequestDto;
import com.gdsc.hearo.domain.member.dto.SignupRequestDto;
import com.gdsc.hearo.domain.member.entity.Member;
import com.gdsc.hearo.domain.member.repository.MemberRepository;
import com.gdsc.hearo.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Member signup(SignupRequestDto request) {

        Member member = Member.builder()
                .username(request.getUsername())
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .loginType(Member.LoginType.DEFAULT)
                .build();

        memberRepository.save(member);

        return member;
    }

    @Transactional
    public Member login(LoginRequestDto request) {
        String loginId = request.getLoginId();
        String password = request.getPassword();

        Member member = memberRepository.findByLoginId(loginId);

        if (member == null) {
            throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
        }

        // 패스워드 일치 여부 판단
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }


        return member;
    }
}
