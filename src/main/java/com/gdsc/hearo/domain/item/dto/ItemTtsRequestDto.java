package com.gdsc.hearo.domain.item.dto;

import com.gdsc.hearo.global.common.VoiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemTtsRequestDto {

    private String itemUrl;
    private VoiceType voiceType;
}
