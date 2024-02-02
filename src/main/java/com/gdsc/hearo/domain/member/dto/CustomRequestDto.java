package com.gdsc.hearo.domain.member.dto;

import com.gdsc.hearo.domain.member.entity.MemberSetting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomRequestDto {
    private byte disabilityType;
    private int fontSize;
    private MemberSetting.VoiceType voiceType;
}
