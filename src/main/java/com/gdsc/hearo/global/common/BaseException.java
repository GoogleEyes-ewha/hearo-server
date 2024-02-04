package com.gdsc.hearo.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BaseException extends Exception {
    public BaseResponseStatus status;
}
