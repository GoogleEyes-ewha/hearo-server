package com.gdsc.hearo.domain.review.controller;

import com.gdsc.hearo.domain.review.dto.ReviewDto;
import com.gdsc.hearo.domain.review.dto.ReviewListResponseDto;
import com.gdsc.hearo.domain.review.service.ReviewService;
import com.gdsc.hearo.global.common.BaseResponse;
import com.gdsc.hearo.global.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    // [Get] 전체 리뷰 조회
    @GetMapping("/{itemId}/list")
    public BaseResponse<?> getReviewList(@PathVariable Long itemId) {

        List<ReviewDto> reviewList = reviewService.getReviewLists(itemId);
        ReviewListResponseDto reviewListResponseDto = new ReviewListResponseDto(reviewList.size(), reviewList);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, reviewListResponseDto);
    }


}
