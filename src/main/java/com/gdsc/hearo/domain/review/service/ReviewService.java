package com.gdsc.hearo.domain.review.service;

import com.gdsc.hearo.domain.item.entity.Item;
import com.gdsc.hearo.domain.item.repository.ItemRepository;
import com.gdsc.hearo.domain.member.entity.MemberSetting;
import com.gdsc.hearo.domain.member.repository.MemberSettingRepository;
import com.gdsc.hearo.domain.review.dto.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    /* Gemini 관련 */
    @Qualifier("geminiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

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
    public void saveReviewTTS(@Nullable CustomUserDetails user, Long itemId, ReviewTTSDto request) throws BaseException {
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

    public ReviewTTSDto getReviewTTS(@Nullable CustomUserDetails user, Long itemId) throws BaseException {
        if (user != null) { // 로그인한 경우

            Long settingId = user.getMember().getMemberSetting().getSettingId();
            MemberSetting memberSetting = memberSettingRepository.findById(settingId).orElse(null);
            VoiceType voiceType = memberSetting.getVoiceType();

            // 로그인한 사용자 음성 타입에 맞는 최신 음성 반환
            ReviewTts positiveReviewTts = reviewTtsRepository.findTopByItemItemIdAndReviewTypeAndVoiceTypeOrderByCreatedAtDesc(
                    itemId, ReviewTts.ReviewType.POSITIVE, voiceType);
            ReviewTts negativeReviewTts = reviewTtsRepository.findTopByItemItemIdAndReviewTypeAndVoiceTypeOrderByCreatedAtDesc(
                    itemId, ReviewTts.ReviewType.NEGATIVE, voiceType);

            if (positiveReviewTts == null || negativeReviewTts == null) {
                throw new BaseException(BaseResponseStatus.NO_REVIEW_TTS_FILE);
            }

            ReviewTTSDto reviewTTSDto = ReviewTTSDto.builder()
                    .positiveReviewUrl(positiveReviewTts.getTtsFile())
                    .negativeReviewUrl(negativeReviewTts.getTtsFile())
                    .build();

            return reviewTTSDto;
        } else {

            ReviewTts positiveReviewTts = reviewTtsRepository.findTopByItemItemIdAndReviewTypeAndVoiceTypeOrderByCreatedAtDesc(
                    itemId, ReviewTts.ReviewType.POSITIVE, VoiceType.MALE_VOICE);
            ReviewTts negativeReviewTts = reviewTtsRepository.findTopByItemItemIdAndReviewTypeAndVoiceTypeOrderByCreatedAtDesc(
                    itemId, ReviewTts.ReviewType.NEGATIVE, VoiceType.MALE_VOICE);

            if (positiveReviewTts == null || negativeReviewTts == null) {
                throw new BaseException(BaseResponseStatus.NO_REVIEW_TTS_FILE);
            }

            ReviewTTSDto reviewTTSDto = ReviewTTSDto.builder()
                    .positiveReviewUrl(positiveReviewTts.getTtsFile())
                    .negativeReviewUrl(negativeReviewTts.getTtsFile())
                    .build();

            return reviewTTSDto;
        }
    }

    public SummaryResponseDto getReviewSummary(Long itemId) throws BaseException {
        // itemId에 맞는 Review 엔티티 리스트
        List<Review> reviewList = reviewRepository.findByItemItemId(itemId);

        if (reviewList.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NO_REVIEW_CONTENT);
        }

        /*
         *   아래와 같은 포맷으로 프롬프트에 넣을 리뷰 텍스트 리스트 생성
         *
         *   R1: (review content 1)
         *   R2: (review content 2)
         *            ...
         *   R100: (review content 100)
         */
        List<String> reviewTextList = reviewList.stream()
                .map(review -> "R" + (reviewList.indexOf(review) + 1) + ": " + review.getContent())
                .toList();

        // 프롬프트 생성
        String prompt = String.format(
                "Understand the content of the list of reviews and then restructure the review contents to provide a summarized overall opinion in one or two sentence. This summary should be divided into a positive review summary and a negative review summary. Tag these summaries with [POS] for positive and [NEG] for negative. Each summary can be composed of up to 200 words.%n%nReview list: %s",
                String.join("\n", reviewTextList)
        );

        // Gemini에 리뷰 요약 요청 전송
        String requestUrl = apiUrl + "?key=" + geminiApiKey;

        ChatRequest request = new ChatRequest(prompt);
        ChatResponse response = restTemplate.postForObject(requestUrl, request, ChatResponse.class);

        String reviewSummary = response.getCandidates().get(0).getContent().getParts().get(0).getText().toString();

        String[] reviews = reviewSummary.split("\n"); // 긍정적인 리뷰, 부정적인 리뷰 구분

        String positiveReview = "";
        String negativeReview = "";

        for (String review : reviews) {
            String trimmedReview = review.trim();
            if (review.trim().startsWith("[POS]")) {
                positiveReview = trimmedReview.substring("[POS]".length()).trim();
            } else if (review.trim().startsWith("[NEG]")) {
                negativeReview = trimmedReview.substring("[NEG]".length()).trim();
            }
        }

        SummaryResponseDto summaryResponseDto = SummaryResponseDto.builder()
                .positiveSummary(positiveReview)
                .negativeSummary(negativeReview)
                .build();

        return summaryResponseDto;
    }
}
