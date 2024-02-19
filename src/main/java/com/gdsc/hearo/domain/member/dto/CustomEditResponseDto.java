package com.gdsc.hearo.domain.member.dto;

import com.gdsc.hearo.domain.member.entity.MemberSetting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomEditResponseDto {
    private int fontSize;
    private MemberSetting.ScreenType screenType;
    private MemberSetting.ComponentType componentType;
}
