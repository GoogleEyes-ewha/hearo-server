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


    @Builder
    public BaseResponse(BaseResponseStatus status, T result) {
        this.code = status.getCode();
        this.inSuccess = status.isInSuccess();
        this.message = status.getMessage();

        this.result = result;
    }

}
