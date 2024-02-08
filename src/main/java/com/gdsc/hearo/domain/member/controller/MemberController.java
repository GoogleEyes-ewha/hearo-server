package com.gdsc.hearo.domain.member.controller;

import com.gdsc.hearo.domain.member.dto.*;
import com.gdsc.hearo.domain.member.entity.Member;
import com.gdsc.hearo.domain.member.entity.MemberSetting;
import com.gdsc.hearo.domain.member.service.MemberService;
import com.gdsc.hearo.domain.member.service.MemberSettingService;
import com.gdsc.hearo.global.common.BaseException;
import com.gdsc.hearo.global.common.BaseResponse;
import com.gdsc.hearo.global.common.BaseResponseStatus;
import com.gdsc.hearo.global.security.CustomUserDetails;
import com.gdsc.hearo.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        try {
            Member member = memberService.signup(request);

            String accessToken = jwtUtil.createAccessToken(member.getLoginId());
            String refreshToken = jwtUtil.createRefreshToken(member.getLoginId());

            LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken, refreshToken);

            return new BaseResponse<>(BaseResponseStatus.SUCCESS, loginResponseDto);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [Post] 일반 로그인
    @PostMapping("/login")
    public BaseResponse<?> signin(@RequestBody LoginRequestDto request) {
        try {
            Member member = memberService.login(request);

            String accessToken = jwtUtil.createAccessToken(member.getLoginId());
            String refreshToken = jwtUtil.createRefreshToken(member.getLoginId());

            LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken, refreshToken);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, loginResponseDto);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [Post] 액세스 토큰 재발급
    @PostMapping("/reissue")
    public BaseResponse<?> refreshToken(@RequestBody ReissueRequestDto request) {
        try {
            LoginResponseDto loginResponseDto = memberService.reissue(request);

            return new BaseResponse<>(BaseResponseStatus.SUCCESS, loginResponseDto);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [Get] 사용자 정보 조회
    @GetMapping("/info")
    public BaseResponse<?> userInfo(@AuthenticationPrincipal CustomUserDetails user) {
        try {
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, memberService.getUserInfo(user.getMember()));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    // [Post] 사용자 맞춤 설정 등록
    @PostMapping("/custom")
    public BaseResponse<?> customSetting(@AuthenticationPrincipal CustomUserDetails user, @RequestBody CustomRequestDto request) {

        memberSettingService.postUserCustom(user.getMember(), request);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "사용자 맞춤 설정 되었습니다.");
    }

    // [Get] 사용자 맞춤 설정 조회
    @GetMapping("/custom")
    public BaseResponse<?> getCustomSetting(@AuthenticationPrincipal CustomUserDetails user) {
        MemberSetting setting = memberSettingService.getUserCustom(user.getMember());
        CustomResponseDto customResponseDto = new CustomResponseDto(setting.getDisabilityType(), setting.getFontSize(), setting.getVoiceType(), setting.getScreenType(), setting.getComponentType());

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, customResponseDto);
    }

    // [Patch] 사용자 맞춤 설정 수정
    @PatchMapping("/custom/edit")
    public BaseResponse<?> editCustomSetting(@AuthenticationPrincipal CustomUserDetails user, @RequestBody CustomEditResponseDto request) {
        memberSettingService.editUserCustom(user.getMember(), request);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "사용자 맞춤 설정이 수정되었습니다.");
    }

}
