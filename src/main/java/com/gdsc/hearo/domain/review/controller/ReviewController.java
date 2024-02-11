package com.gdsc.hearo.domain.review.controller;

import com.gdsc.hearo.domain.review.dto.ReviewDto;
import com.gdsc.hearo.domain.review.dto.ReviewListResponseDto;
import com.gdsc.hearo.domain.review.dto.ReviewTTSDto;
import com.gdsc.hearo.domain.review.service.ReviewService;
import com.gdsc.hearo.global.common.BaseException;
import com.gdsc.hearo.global.common.BaseResponse;
import com.gdsc.hearo.global.common.BaseResponseStatus;
import com.gdsc.hearo.global.security.CustomUserDetails;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    // [Get] 전체 리뷰 조회
    @GetMapping("/{itemId}/list")
    public BaseResponse<?> getReviewList(@PathVariable Long itemId) {
        try {
            List<ReviewDto> reviewList = reviewService.getReviewLists(itemId);
            ReviewListResponseDto reviewListResponseDto = new ReviewListResponseDto(reviewList.size(), reviewList);

            return new BaseResponse<>(BaseResponseStatus.SUCCESS, reviewListResponseDto);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [Get] 리뷰 요약 텍스트 조회
    @GetMapping("/{itemId}")
    public BaseResponse<?> reviewSummary(@PathVariable Long itemId) {
        try {
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, reviewService.getReviewSummary(itemId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [Post] 리뷰 tts 파일 저장
    @PostMapping("/tts/{itemId}")
    public BaseResponse<?> postReviewTTSFile(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long itemId, @RequestBody ReviewTTSDto request) {
        try {
            if (user != null) { // 로그인 한 경우
                reviewService.saveReviewTTS(user, itemId, request);

                return new BaseResponse<>(BaseResponseStatus.SUCCESS, "음성 파일이 저장되었습니다.");

            } else { // 로그인하지 않은 경우
                reviewService.saveReviewTTS(null, itemId, request);

                return new BaseResponse<>(BaseResponseStatus.SUCCESS, "음성 파일이 저장되었습니다.");
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [Get] 리뷰 tts 파일 조회
    @GetMapping("/tts/{itemId}")
    public BaseResponse<?> getReviewTTSFile(@Nullable @AuthenticationPrincipal CustomUserDetails user, @PathVariable Long itemId) {
        try {
            if (user != null) { // 로그인 한 경우
                ReviewTTSDto reviewTts = reviewService.getReviewTTS(user, itemId);

                return new BaseResponse<>(BaseResponseStatus.SUCCESS, reviewTts);
            } else { // 로그인하지 않은 경우
                ReviewTTSDto reviewTts = reviewService.getReviewTTS(null, itemId);

                return new BaseResponse<>(BaseResponseStatus.SUCCESS, reviewTts);
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
