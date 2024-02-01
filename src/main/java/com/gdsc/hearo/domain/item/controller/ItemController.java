package com.gdsc.hearo.domain.item.controller;

import com.gdsc.hearo.domain.item.dto.CategoryResponseDto;
import com.gdsc.hearo.domain.item.service.ItemService;
import com.gdsc.hearo.domain.item.service.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
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


    @GetMapping("/{categoryId}")
    public ResponseEntity<Map<String,Object>> getItemByCategory(@PathVariable(name="categoryId") Long categoryId){
        List<CategoryResponseDto> itemList = itemService.getItemByCategory(categoryId);
        Map<String,Object> response = new HashMap<>();
        response.put("code",1000);
        response.put("inSuccess",true);
        response.put("message","요청에 성공하셨습니다.");

        response.put("result",Map.of("itemCount",itemList.size(),"itemList",itemList));
        return ResponseEntity.ok(response);
    }
}
