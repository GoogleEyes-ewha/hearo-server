package com.gdsc.hearo.global.common;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    // 1000 : Request 성공
    SUCCESS(1000, true, "요청에 성공하였습니다."),

    // 2000 : Request 오류
    NO_CONTENT(2000,false ,"콘텐츠가 없습니다." ),

    // 3000 : Response 오류
    RESPONSE_ERROR(3000, false, "값을 불러오는데 실패하였습니다."),

    INVALID_USER_ID(3010, false, "아이디가 존재하지 않습니다."),
    FAILED_TO_LOGIN(3011, false, "비밀번호가 일치하지 않습니다."),

    INTERNAL_SERVER_ERROR(9000, false, "서버 오류가 발생했습니다.");

    // ==========================
    private int code;
    private boolean inSuccess;
    private String message;


    /*
        해당되는 코드 매핑
        @param code
        @param inSuccess
        @param message

     */
    BaseResponseStatus(int code, boolean inSuccess, String message) {
        this.inSuccess = inSuccess;
        this.code = code;
        this.message = message;
    }
}
