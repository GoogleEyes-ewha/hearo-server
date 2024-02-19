package com.gdsc.hearo.domain.member.dto;

import com.gdsc.hearo.domain.member.entity.MemberSetting;
import com.gdsc.hearo.global.common.VoiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomResponseDto {
    private byte disabilityType;
    private int fontSize;
    private VoiceType voiceType;
    private MemberSetting.ScreenType screenType;
    private MemberSetting.ComponentType componentType;
}
