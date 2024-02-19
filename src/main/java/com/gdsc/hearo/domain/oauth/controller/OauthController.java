package com.gdsc.hearo.domain.oauth.controller;

import com.gdsc.hearo.domain.member.dto.LoginResponseDto;
import com.gdsc.hearo.domain.member.entity.Member;
import com.gdsc.hearo.domain.oauth.dto.GoogleProfile;
import com.gdsc.hearo.domain.oauth.service.OauthService;
import com.gdsc.hearo.global.common.BaseException;
import com.gdsc.hearo.global.common.BaseResponse;
import com.gdsc.hearo.global.common.BaseResponseStatus;
import com.gdsc.hearo.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Slf4j
public class OauthController {

    private final OauthService oauthService;
    private final JwtUtil jwtUtil;

    @GetMapping(value = "/google")
    public void socialLoginType() {
        oauthService.request();
    }
    @GetMapping(value = "/google/callback")
    public BaseResponse<?> googleLogin(@RequestParam(name = "code") String code) {

        try {
            GoogleProfile googleProfile = oauthService.requestGoogleProfile(code);
            Member member = oauthService.googleLogin(googleProfile);

            String accessToken = jwtUtil.createAccessToken(member.getLoginId());
            String refreshToken = jwtUtil.createRefreshToken(member.getLoginId());

            LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken, refreshToken);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, loginResponseDto);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
