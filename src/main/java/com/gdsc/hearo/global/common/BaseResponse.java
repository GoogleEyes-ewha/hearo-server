package com.gdsc.hearo.global.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BaseResponse<T> {

    private int code;
    private boolean inSuccess;
    private String message;
    private T result;


    // 요청에 성공한 경우
    @Builder
    public BaseResponse(BaseResponseStatus status, T result) {
        this.code = status.getCode();
        this.inSuccess = status.isInSuccess();
        this.message = status.getMessage();

        this.result = result;
    }

    // 요청에 실패한 경우
    @Builder
    public BaseResponse(BaseResponseStatus status) {
        this.code = status.getCode();
        this.inSuccess = status.isInSuccess();
        this.message = status.getMessage();
    }

}
