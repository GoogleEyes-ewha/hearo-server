package com.gdsc.hearo.domain.oauth.dto;

import lombok.Data;

@Data
public class GoogleOAuthToken {
    private String access_token;
    private String expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
