package com.gdsc.hearo.global.common;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /*
        1000 : Request 성공
     */
    SUCCESS(1000, true, "요청에 성공하였습니다."),
    JWT_VERIFIED(1000, true, "유효한 액세스 토큰입니다."),

    /*
        2000~ : Request 오류
     */
    // 2000~ : user 관련 오류
    INVALID_TOKEN(2000, false, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(2001, false, "만료된 토큰 정보입니다."),
    UNSUPPORTED_TOKEN(2002, false, "지원하지 않는 토큰 방식입니다."),


    // =====================================
    /*
        3000~ : Response 오류
    */
    // 3000~ : user 관련 오류
    RESPONSE_ERROR(3000, false, "값을 불러오는데 실패하였습니다."),
    INVALID_USER_ID(3001, false, "아이디가 존재하지 않습니다."),
    FAILED_TO_LOGIN(3002, false, "비밀번호가 일치하지 않습니다."),
    DUPICATE_USER_ID(3003, false, "이미 존재하는 아이디입니다."),
    ACCESS_DENIED(3004, false, "알 수 없는 이유로 요청이 거절되었습니다."),
    EXPIRED_REFRESH_TOKEN(3005, false, "만료된 refresh token 입니다."),
    FAILED_TO_FIND_USER(3006,false,"존재하지 않는 회원입니다."),

    // 3100~ : item 관련 오류
    NO_CONTENT(3100, false, "상품이 존재하지 않습니다."),

    // 3200~ : review 관련 오류
    NO_REVIEW_CONTENT(3200, false, "리뷰가 존재하지 않습니다."),
    INVALID_REVIEW_SUMMARY(3201, false, "오류가 발생했습니다. 다시 요청해 주세요."),

    // 3300~ : TTS 관련 오류
    NO_REVIEW_TTS_FILE(3300, false, "리뷰 요약 음성 파일이 존재하지 않습니다."),

    //3400~ : wishList 관련 오류
    DUPICATE_ITEM(3400,false,"이미 위시리스트에 존재하는 상품입니다."),


    // =====================================

    // 그 외 오류
    INTERNAL_SERVER_ERROR(9000, false, "서버 오류가 발생했습니다.");


    // =====================================
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
