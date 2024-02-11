package com.gdsc.hearo.domain.item.service;

import com.gdsc.hearo.domain.item.dto.ItemTtsFileResponseDto;
import com.gdsc.hearo.domain.item.dto.ItemTtsRequestDto;
import com.gdsc.hearo.domain.item.dto.ItemTtsResponseDto;
import com.gdsc.hearo.domain.item.entity.Item;
import com.gdsc.hearo.domain.item.entity.ItemTts;
import com.gdsc.hearo.domain.item.repository.ItemRepository;
import com.gdsc.hearo.domain.item.repository.ItemTtsRepository;
import com.gdsc.hearo.domain.member.entity.MemberSetting;
import com.gdsc.hearo.domain.member.repository.MemberSettingRepository;
import com.gdsc.hearo.domain.review.repository.ReviewTtsRepository;
import com.gdsc.hearo.global.common.BaseException;
import com.gdsc.hearo.global.common.VoiceType;
import com.gdsc.hearo.global.security.CustomUserDetails;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemTtsService {

    private final ItemTtsRepository itemTtsRepository;
    private final MemberSettingRepository memberSettingRepository;
    private final ReviewTtsRepository reviewTtsRepository;
    private final ItemRepository itemRepository;

    //상품 음성파일 저장
    public ItemTtsResponseDto saveItemTTS(@Nullable CustomUserDetails user, Long itemId, ItemTtsRequestDto itemTtsRequestDto) throws BaseException {
        if (user != null) { // 로그인한 경우
            Item item = itemRepository.findById(itemId).orElse(null);

            Long settingId = user.getMember().getMemberSetting().getSettingId();
            MemberSetting memberSetting = memberSettingRepository.findById(settingId).orElse(null);
            VoiceType voiceType = memberSetting.getVoiceType();

            ItemTts itemTts = ItemTts.builder()
                    .item(item)
                    .ttsFile(itemTtsRequestDto.getItemUrl())
                    .voiceType(voiceType)
                    .build();

            ItemTts savedItemTts = itemTtsRepository.save(itemTts);

            return ItemTtsResponseDto.builder()
                    .result("상품 음성파일이 저장되었습니다.")
                    .build();

        } else { // 로그인하지 않은 경우
            Item item = itemRepository.findById(itemId).orElse(null);

            ItemTts itemTts = ItemTts.builder()
                    .item(item)
                    .ttsFile(itemTtsRequestDto.getItemUrl())
                    .voiceType(VoiceType.MALE_VOICE)
                    .build();

            ItemTts savedItemTts = itemTtsRepository.save(itemTts);

            return ItemTtsResponseDto.builder()
                    .result("상품 음성파일이 저장되었습니다.")
                    .build();

        }
    }

    public ItemTtsFileResponseDto getItemTts(@Nullable CustomUserDetails user,Long itemId) {

        if (user != null) { // 로그인한 경우

            Long settingId = user.getMember().getMemberSetting().getSettingId();
            MemberSetting memberSetting = memberSettingRepository.findById(settingId).orElse(null);
            VoiceType voiceType=  memberSetting.getVoiceType();

            ItemTts itemTts = itemTtsRepository.findTopByItemItemIdAndVoiceTypeOrderByCreatedAtDesc(
                    itemId,voiceType);

            if(itemTts != null){
                return ItemTtsFileResponseDto.builder()
                        .itemUrl(itemTts.getTtsFile())
                        .build();
            }
            else{
                return ItemTtsFileResponseDto.builder()
                        .itemUrl("")
                        .build();
            }

        } else {
             VoiceType voiceType= VoiceType.MALE_VOICE;

            ItemTts itemTts = itemTtsRepository.findTopByItemItemIdAndVoiceTypeOrderByCreatedAtDesc(
                    itemId,voiceType);

            if(itemTts != null){
                return ItemTtsFileResponseDto.builder()
                        .itemUrl(itemTts.getTtsFile())
                        .build();
            }
            else{
                return ItemTtsFileResponseDto.builder()
                        .itemUrl("")
                        .build();
            }
        }
    }


}
