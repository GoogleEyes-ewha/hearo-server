package com.gdsc.hearo.domain.oauth.service;

import com.gdsc.hearo.domain.member.entity.Member;
import com.gdsc.hearo.domain.member.repository.MemberRepository;
import com.gdsc.hearo.domain.oauth.dto.GoogleOAuthToken;
import com.gdsc.hearo.domain.oauth.dto.GoogleProfile;
import com.gdsc.hearo.domain.oauth.service.social.GoogleOauth;
import com.gdsc.hearo.global.common.BaseException;
import com.gdsc.hearo.global.common.BaseResponseStatus;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final GoogleOauth googleOauth;
    private final HttpServletResponse response;
    private final MemberRepository memberRepository;

    public void request() {
        String redirectURL = googleOauth.getOauthRedirectURL();
        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public GoogleProfile requestGoogleProfile(String code) {
        GoogleOAuthToken googleOAuthToken = googleOauth.requestAccessToken(code);
        return googleOauth.requestGoogleProfile(googleOAuthToken);
    }

    public Member googleLogin(GoogleProfile googleProfile) throws BaseException {
        String email = googleProfile.getEmail();
        String googleId = googleProfile.getId();
        String username = googleProfile.getName();

        Member member = memberRepository.findByLoginId(email);

        Member newMember = Member.builder()
                .username(username)
                .loginId(email)
                .GoogleId(googleId)
                .loginType(Member.LoginType.GOOGLE)
                .build();

        // 저장 안 된 사용자일 경우
        if (member == null) {
            memberRepository.save(newMember);
        }

        return newMember;
    }
}
