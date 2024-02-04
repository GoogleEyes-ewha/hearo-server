package com.gdsc.hearo.domain.review.service;

import com.gdsc.hearo.domain.review.dto.ReviewDto;
import com.gdsc.hearo.domain.review.entity.Review;
import com.gdsc.hearo.domain.review.repository.ReviewRepository;
import com.gdsc.hearo.global.common.BaseException;
import com.gdsc.hearo.global.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<ReviewDto> getReviewLists(Long itemId) throws BaseException {
        List<Review> reviewList = reviewRepository.findByItemItemId(itemId);

        if (reviewList.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NO_REVIEW_CONTENT);
        }

        return reviewList.stream()
                .map(review -> new ReviewDto(review.getReviewId(), review.getContent()))
                .collect(Collectors.toList());
    }
}
