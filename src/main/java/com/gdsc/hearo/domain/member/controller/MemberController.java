package com.gdsc.hearo.domain.member.controller;

import com.gdsc.hearo.domain.member.dto.LoginRequestDto;
import com.gdsc.hearo.domain.member.dto.LoginResponseDto;
import com.gdsc.hearo.domain.member.dto.SignupRequestDto;
import com.gdsc.hearo.domain.member.entity.Member;
import com.gdsc.hearo.domain.member.service.MemberService;
import com.gdsc.hearo.global.common.BaseResponse;
import com.gdsc.hearo.global.common.BaseResponseStatus;
import com.gdsc.hearo.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public BaseResponse<LoginResponseDto> signup(@RequestBody SignupRequestDto request) {
        Member member = memberService.signup(request);

        String accessToken = jwtUtil.createAccessToken(member.getLoginId());
        String refreshToken = jwtUtil.createRefreshToken(member.getLoginId());

        LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken, refreshToken);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, loginResponseDto);
    }

    @PostMapping("/login")
    public BaseResponse<?> signin(@RequestBody LoginRequestDto request) {
        try {
            Member member = memberService.login(request);

            String accessToken = jwtUtil.createRefreshToken(member.getLoginId());
            String refreshToken = jwtUtil.createRefreshToken(member.getLoginId());

            LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken, refreshToken);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, loginResponseDto);
        } catch (Exception e) {
            if (e instanceof UsernameNotFoundException) {
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_ID, null);
            } else if (e instanceof BadCredentialsException) {
                return new BaseResponse<>(BaseResponseStatus.FAILED_TO_LOGIN, null);
            } else {
                // 기타 예외에 대한 기본 처리
                return new BaseResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR, null);
            }
        }
    }

}
