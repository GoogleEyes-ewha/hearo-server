package com.gdsc.hearo.domain.item.controller;

import com.gdsc.hearo.domain.item.dto.CategoryResponseDto;
import com.gdsc.hearo.domain.item.service.ItemService;
import com.gdsc.hearo.global.common.BaseResponse;
import com.gdsc.hearo.global.common.BaseResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/item")
public class ItemController {
    
    private final ItemService itemService;
    
    @Autowired
    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }


    /*@GetMapping("/{categoryId}")
    public ResponseEntity<Map<String,Object>> getItemByCategory(@PathVariable(name="categoryId") Long categoryId){
        List<CategoryResponseDto> itemList = itemService.getItemByCategory(categoryId);
        Map<String,Object> response = new HashMap<>();
        response.put("code",1000);
        response.put("inSuccess",true);
        response.put("message","요청에 성공하셨습니다.");

        response.put("result",Map.of("itemCount",itemList.size(),"itemList",itemList));
        return ResponseEntity.ok(response);
    }*/
    @GetMapping("/{categoryId}")
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
    }


}
