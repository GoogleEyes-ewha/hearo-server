package com.gdsc.hearo.domain.review.service;

import com.gdsc.hearo.domain.item.entity.Item;
import com.gdsc.hearo.domain.item.repository.ItemRepository;
import com.gdsc.hearo.domain.member.entity.MemberSetting;
import com.gdsc.hearo.domain.member.repository.MemberSettingRepository;
import com.gdsc.hearo.domain.review.dto.ReviewDto;
import com.gdsc.hearo.domain.review.dto.ReviewTTSRequestDto;
import com.gdsc.hearo.domain.review.entity.Review;
import com.gdsc.hearo.domain.review.entity.ReviewTts;
import com.gdsc.hearo.domain.review.repository.ReviewRepository;
import com.gdsc.hearo.domain.review.repository.ReviewTtsRepository;
import com.gdsc.hearo.global.common.BaseException;
import com.gdsc.hearo.global.common.BaseResponseStatus;
import com.gdsc.hearo.global.common.VoiceType;
import com.gdsc.hearo.global.security.CustomUserDetails;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberSettingRepository memberSettingRepository;
    private final ReviewTtsRepository reviewTtsRepository;
    private final ItemRepository itemRepository;

    public List<ReviewDto> getReviewLists(Long itemId) throws BaseException {
        List<Review> reviewList = reviewRepository.findByItemItemId(itemId);

        if (reviewList.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NO_REVIEW_CONTENT);
        }

        return reviewList.stream()
                .map(review -> new ReviewDto(review.getReviewId(), review.getContent()))
                .collect(Collectors.toList());
    }

    /*
        리뷰 TTS 음성 파일 저장 (긍정적인 리뷰 요약, 부정적인 리뷰 요약)
     */
    public void saveReviewTTS(@Nullable CustomUserDetails user, Long itemId, ReviewTTSRequestDto request) throws BaseException {
        if (user != null) { // 로그인한 경우
            Item item = itemRepository.findById(itemId).orElse(null);

            Long settingId = user.getMember().getMemberSetting().getSettingId();
            MemberSetting memberSetting = memberSettingRepository.findById(settingId).orElse(null);
            VoiceType voiceType = memberSetting.getVoiceType();

            ReviewTts positiveReviewTts = ReviewTts.builder()
                    .item(item)
                    .ttsFile(request.getPositiveReviewUrl())
                    .reviewType(ReviewTts.ReviewType.POSITIVE)
                    .voiceType(voiceType)
                    .build();

            ReviewTts negativeReviewTts = ReviewTts.builder()
                    .item(item)
                    .reviewType(ReviewTts.ReviewType.NEGATIVE)
                    .ttsFile(request.getNegativeReviewUrl())
                    .voiceType(voiceType)
                    .build();

            List<ReviewTts> reviewTtsList = Arrays.asList(positiveReviewTts, negativeReviewTts);

            reviewTtsRepository.saveAll(reviewTtsList);

        } else { // 로그인하지 않은 경우
            Item item = itemRepository.findById(itemId).orElse(null);

            ReviewTts positiveReviewTts = ReviewTts.builder()
                    .item(item)
                    .reviewType(ReviewTts.ReviewType.POSITIVE)
                    .ttsFile(request.getPositiveReviewUrl())
                    .voiceType(VoiceType.MALE_VOICE)
                    .build();

            ReviewTts negativeReviewTts = ReviewTts.builder()
                    .item(item)
                    .reviewType(ReviewTts.ReviewType.NEGATIVE)
                    .ttsFile(request.getNegativeReviewUrl())
                    .voiceType(VoiceType.MALE_VOICE)
                    .build();

            List<ReviewTts> reviewTtsList = Arrays.asList(positiveReviewTts, negativeReviewTts);

            reviewTtsRepository.saveAll(reviewTtsList);

        }

    }
}
