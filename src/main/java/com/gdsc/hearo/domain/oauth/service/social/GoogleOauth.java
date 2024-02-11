package com.gdsc.hearo.domain.oauth.service.social;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.hearo.domain.oauth.dto.GoogleOAuthToken;
import com.gdsc.hearo.domain.oauth.dto.GoogleProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleOauth {

    @Value("${spring.google.url}")
    private String GOOGLE_SNS_BASE_URL;
    @Value("${spring.google.client-id}")
    private String GOOGLE_SNS_CLIENT_ID;
    @Value("${spring.google.callback}")
    private String GOOGLE_SNS_CALLBACK_URL;
    @Value("${spring.google.secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;
    @Value("${spring.google.token}")
    private String GOOGLE_SNS_TOKEN_BASE_URL;

    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("scope", "profile email");
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return GOOGLE_SNS_BASE_URL + "?" + parameterString;
    }

    public GoogleOAuthToken requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        GoogleOAuthToken oAuthToken = null;

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> response =
                restTemplate.postForEntity(GOOGLE_SNS_TOKEN_BASE_URL, params, String.class);

        try {
            oAuthToken = objectMapper.readValue(response.getBody(), GoogleOAuthToken.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return oAuthToken;
    }

    public GoogleProfile requestGoogleProfile(GoogleOAuthToken oAuthToken) {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + oAuthToken.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> googleProfileRequest = new HttpEntity<>(header);

        ResponseEntity<String> googleProfileResponse = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                googleProfileRequest,
                String.class
        );

        GoogleProfile googleProfile = null;

        try {
            googleProfile = objectMapper.readValue(googleProfileResponse.getBody(), GoogleProfile.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return googleProfile;
    }
}
