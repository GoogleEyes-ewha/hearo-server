package com.gdsc.hearo.domain.member.service;

import com.gdsc.hearo.domain.member.dto.LoginRequestDto;
import com.gdsc.hearo.domain.member.dto.SignupRequestDto;
import com.gdsc.hearo.domain.member.entity.Member;
import com.gdsc.hearo.domain.member.repository.MemberRepository;
import com.gdsc.hearo.global.common.BaseException;
import com.gdsc.hearo.global.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member signup(SignupRequestDto request) throws BaseException {
        String loginId = request.getLoginId();

        Member member = memberRepository.findByLoginId(loginId);

        if (member != null) {
            throw new BaseException(BaseResponseStatus.DUPICATE_USER_ID);
        }

        Member newMember = Member.builder()
                .username(request.getUsername())
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .loginType(Member.LoginType.DEFAULT)
                .build();

        memberRepository.save(newMember);

        return newMember;
    }

    @Transactional
    public Member login(LoginRequestDto request) throws BaseException {
        String loginId = request.getLoginId();
        String password = request.getPassword();

        Member member = memberRepository.findByLoginId(loginId);

        if (member == null) {
            throw new BaseException(BaseResponseStatus.INVALID_USER_ID);
        }

        // 패스워드 일치 여부 판단
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_LOGIN);
        }

        return member;
    }
}
