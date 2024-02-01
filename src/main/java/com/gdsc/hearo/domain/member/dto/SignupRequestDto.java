package com.gdsc.hearo.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;
}
