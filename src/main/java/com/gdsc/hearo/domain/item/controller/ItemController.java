package com.gdsc.hearo.domain.item.controller;

import com.gdsc.hearo.domain.item.dto.*;
import com.gdsc.hearo.domain.item.service.ItemService;
import com.gdsc.hearo.domain.item.service.WishService;
import com.gdsc.hearo.global.common.BaseResponse;
import com.gdsc.hearo.global.common.BaseResponseStatus;
import com.gdsc.hearo.global.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {
    
    private final ItemService itemService;

    
    @Autowired
    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    /*@GetMapping("/{categoryId}")
    public ResponseEntity<BaseResponse<Map<String, Object>>> getItemByCategory(@PathVariable(name = "categoryId") Long categoryId) {
        BaseResponse<Map<String, Object>> response;

        try {
            List<CategoryResponseDto> itemList = itemService.getItemByCategory(categoryId);
            Map<String, Object> result = new HashMap<>();

            if (itemList.isEmpty()) {
                response = new BaseResponse<>(BaseResponseStatus.NO_CONTENT, null);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }

            result.put("itemCount", itemList.size());
            result.put("itemList", itemList);
            response = new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // 기타 예외에 대한 기본 처리
            response = new BaseResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }*/
    @GetMapping("/{categoryId}")
    public ResponseEntity<BaseResponse<CategoryResponseDto>> getItemByCategory(@PathVariable(name = "categoryId") Long categoryId) {
        BaseResponse<CategoryResponseDto> response;

        try {
            List<ItemDto> itemList = itemService.getItemByCategory(categoryId);
            CategoryResponseDto result = CategoryResponseDto.builder()
                    .itemCount(itemList.size())
                    .list(itemList)
                    .build();

            if (itemList.isEmpty()) {
                response = new BaseResponse<>(BaseResponseStatus.NO_CONTENT, null);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }

            response = new BaseResponse<>(BaseResponseStatus.SUCCESS,result);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // 기타 예외에 따른 기본 처리
            response = new BaseResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping
    public ResponseEntity<BaseResponse<SearchResponseDto>> getItemByKeyword(
            @RequestParam(name = "keyword") String keyword) {
        BaseResponse<SearchResponseDto> response;

        try {
            List<ItemDto> itemList = itemService.getItemByKeyword(keyword);
            SearchResponseDto result = SearchResponseDto.builder()
                    .keyword(keyword)
                    .itemCount(itemList.size())
                    .list(itemList)
                    .build();

            if (itemList.isEmpty()) {
                response = new BaseResponse<>(BaseResponseStatus.NO_CONTENT, null);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }

            response = new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Handle exceptions
            response = new BaseResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
