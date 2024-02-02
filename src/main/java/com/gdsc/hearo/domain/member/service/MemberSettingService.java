package com.gdsc.hearo.domain.member.service;

import com.gdsc.hearo.domain.member.dto.CustomRequestDto;
import com.gdsc.hearo.domain.member.entity.Member;
import com.gdsc.hearo.domain.member.entity.MemberSetting;
import com.gdsc.hearo.domain.member.repository.MemberRepository;
import com.gdsc.hearo.domain.member.repository.MemberSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberSettingService {

    private final MemberRepository memberRepository;
    private final MemberSettingRepository memberSettingRepository;

    public void postUserCustom(Member member, CustomRequestDto request) {

        MemberSetting memberSetting = MemberSetting.builder()
                .member(member)
                .disabilityType(request.getDisabilityType())
                .fontSize(request.getFontSize())
                .voiceType(request.getVoiceType())
                .build();

        member.setMemberSetting(memberSetting);

        memberSettingRepository.save(memberSetting);
        memberRepository.save(member);

    }

    public MemberSetting getUserCustom(Member member) {
        Long settingId = member.getMemberSetting().getSettingId();

        return memberSettingRepository.findById(settingId).orElse(null);
    }
}
