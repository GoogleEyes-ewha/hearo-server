package com.gdsc.hearo.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReissueRequestDto {

    private String accessToken;
    private String refreshToken;

}
