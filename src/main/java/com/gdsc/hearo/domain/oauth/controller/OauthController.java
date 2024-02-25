package com.gdsc.hearo.domain.oauth.controller;

import com.gdsc.hearo.domain.member.entity.Member;
import com.gdsc.hearo.domain.oauth.dto.GoogleProfile;
import com.gdsc.hearo.domain.oauth.service.OauthService;
import com.gdsc.hearo.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

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
    public RedirectView googleLogin(@RequestParam(name = "code") String code) {

        GoogleProfile googleProfile = oauthService.requestGoogleProfile(code);
        Member member = oauthService.googleLogin(googleProfile);

        String accessToken = jwtUtil.createAccessToken(member.getLoginId());
        String refreshToken = jwtUtil.createRefreshToken(member.getLoginId());

        RedirectView redirectView = new RedirectView();

        redirectView.setUrl("https://hearo-hmuri.vercel.app/login/callback?accessToken={accessToken}&refreshToken={refreshToken}");
        redirectView.addStaticAttribute("accessToken", accessToken);
        redirectView.addStaticAttribute("refreshToken", refreshToken);

        return redirectView;

    }
}
