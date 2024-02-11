package com.gdsc.hearo.domain.item.controller;

import com.gdsc.hearo.domain.item.dto.ItemTtsFileResponseDto;
import com.gdsc.hearo.domain.item.dto.ItemTtsRequestDto;
import com.gdsc.hearo.domain.item.dto.ItemTtsResponseDto;
import com.gdsc.hearo.domain.item.service.ItemTtsService;
import com.gdsc.hearo.domain.review.dto.ReviewTTSDto;
import com.gdsc.hearo.global.common.BaseException;
import com.gdsc.hearo.global.common.BaseResponse;
import com.gdsc.hearo.global.common.BaseResponseStatus;
import com.gdsc.hearo.global.common.VoiceType;
import com.gdsc.hearo.global.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("item")
public class ItemTtsController {

    private final ItemTtsService itemTtsService;

    @Autowired
    public ItemTtsController(ItemTtsService itemTtsService){
        this.itemTtsService = itemTtsService;
    }

    //[post]상품 음성 파일 저장
    @PostMapping("/tts/{itemId}")
    public BaseResponse<?> postItemTTSFile(@AuthenticationPrincipal CustomUserDetails user, @PathVariable(name="itemId") Long itemId, @RequestBody ItemTtsRequestDto request) {
        try {
            if (user != null) { // 로그인 한 경우
                // 서비스를 호출하여 음성 파일 저장
                ItemTtsResponseDto result = itemTtsService.saveItemTTS(user, itemId, request);

                return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);

            } else { // 로그인하지 않은 경우
                ItemTtsResponseDto result = itemTtsService.saveItemTTS(null, itemId, request);

                return new BaseResponse<>(BaseResponseStatus.SUCCESS, "음성 파일이 저장되었습니다.");
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 상품 음성 파일 조회
    @GetMapping("/tts/{itemId}")
    public BaseResponse<ItemTtsFileResponseDto> getItemTts(@AuthenticationPrincipal CustomUserDetails user,
                                                           @PathVariable(name = "itemId") Long itemId) {

        try{
            if (user != null) { // 로그인 한 경우
                ItemTtsFileResponseDto result = itemTtsService.getItemTts(user, itemId);
                return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
                }
            else{
                ItemTtsFileResponseDto result = itemTtsService.getItemTts(null,itemId);
                return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null; // 예외처리 이상함..
        }
    }
}
