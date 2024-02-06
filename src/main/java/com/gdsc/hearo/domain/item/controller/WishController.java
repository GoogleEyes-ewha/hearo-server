package com.gdsc.hearo.domain.item.controller;

import com.gdsc.hearo.domain.item.dto.WishListResponseDto;
import com.gdsc.hearo.domain.item.dto.WishRequestDto;
import com.gdsc.hearo.domain.item.dto.WishResponseDto;
import com.gdsc.hearo.domain.item.service.WishService;
import com.gdsc.hearo.global.common.BaseResponse;
import com.gdsc.hearo.global.common.BaseResponseStatus;
import com.gdsc.hearo.global.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item/wish")
public class WishController {

    private final WishService wishService;

    @Autowired
    public WishController(WishService wishService){
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse<WishResponseDto>> addToWishList(@AuthenticationPrincipal CustomUserDetails userDetails,

                                                                       @RequestBody WishRequestDto wishRequestDto){
        BaseResponse<WishResponseDto> response;

        try{
            Long userId = userDetails.getMember().getMemberId();
            WishResponseDto result = wishService.addToWishList(userId, wishRequestDto.getItemId());

            response = new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
            return ResponseEntity.ok(response);

        }catch (Exception e){
            response = new BaseResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<BaseResponse<WishListResponseDto>> getWishList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        BaseResponse<WishListResponseDto> response;

        try {
            Long userId = userDetails.getMember().getMemberId();
            WishListResponseDto result = wishService.getWishList(userId);

            response = new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new BaseResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



}
