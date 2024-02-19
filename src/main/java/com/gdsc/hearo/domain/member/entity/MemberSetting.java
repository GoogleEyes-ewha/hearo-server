package com.gdsc.hearo.domain.member.entity;


import com.gdsc.hearo.global.common.VoiceType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_setting")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class MemberSetting {

    public enum ScreenType {
        MODE1, MODE2
    }

    public enum ComponentType {
        ONE, THREE, SIX
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id", nullable = false)
    private Long settingId; // 기본키

    @Column(name = "disability_type")
    private byte disabilityType;

    @Column(name = "font_size")
    private Integer fontSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "voice_type")
    private VoiceType voiceType = VoiceType.MALE_VOICE;

    @Enumerated(EnumType.STRING)
    @Column(name = "screen_type")
    private ScreenType screenType = ScreenType.MODE1;

    @Enumerated(EnumType.STRING)
    @Column(name = "component_type")
    private ComponentType componentType = ComponentType.SIX;

    @OneToOne(mappedBy = "memberSetting")
    private Member member;


    public void setCustom(Integer fontSize, ScreenType screenType, ComponentType componentType) {
        this.fontSize = fontSize;
        this.screenType = screenType;
        this.componentType = componentType;
    }
}
