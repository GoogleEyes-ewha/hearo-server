package com.gdsc.hearo.domain.member.controller;

import com.gdsc.hearo.domain.member.dto.*;
import com.gdsc.hearo.domain.member.entity.Member;
import com.gdsc.hearo.domain.member.entity.MemberSetting;
import com.gdsc.hearo.domain.member.service.MemberService;
import com.gdsc.hearo.domain.member.service.MemberSettingService;
import com.gdsc.hearo.global.common.BaseResponse;
import com.gdsc.hearo.global.common.BaseResponseStatus;
import com.gdsc.hearo.global.security.CustomUserDetails;
import com.gdsc.hearo.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;
    private final MemberSettingService memberSettingService;
    private final JwtUtil jwtUtil;

    // [Post] 일반 회원가입
    @PostMapping("/signup")
    public BaseResponse<LoginResponseDto> signup(@RequestBody SignupRequestDto request) {
        Member member = memberService.signup(request);

        String accessToken = jwtUtil.createAccessToken(member.getLoginId());
        String refreshToken = jwtUtil.createRefreshToken(member.getLoginId());

        LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken, refreshToken);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, loginResponseDto);
    }

    // [Post] 일반 로그인
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

    // [Post] 사용자 맞춤 설정 등록
    @PostMapping("/custom")
    public BaseResponse<?> customSetting(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody CustomRequestDto request) {

        memberSettingService.postUserCustom(userDetails.getMember(), request);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "사용자 맞춤 설정 되었습니다.");
    }

    // [Get] 사용자 맞춤 설정 조회
    @GetMapping("/custom")
    public BaseResponse<?> getCustomSetting(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberSetting setting = memberSettingService.getUserCustom(userDetails.getMember());
        CustomResponseDto customResponseDto = new CustomResponseDto(setting.getDisabilityType(), setting.getFontSize(), setting.getVoiceType(), setting.getScreenType(), setting.getComponentType());

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, customResponseDto);
    }
}
