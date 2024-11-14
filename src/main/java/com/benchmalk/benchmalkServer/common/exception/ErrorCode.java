package com.benchmalk.benchmalkServer.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /*
     * 400 BAD_REQUEST: 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    BAD_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "파일 형식이 올바르지 않습니다."),
    ROLE_BAD_REQUEST(HttpStatus.BAD_REQUEST, "역할이 올바르지 않습니다."),
    PASSWORD_BAD_REQUEST(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),

    /*
     * 403 FORBIDDEN: 승인을 거부함
     */
    ROLE_FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없는 역할입니다."),

    /*
     * 404 NOT_FOUND: 리소스를 찾을 수 없음
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    MODEL_NOT_FOUND(HttpStatus.NOT_FOUND, "모델을 찾을 수 없습니다."),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."),
    PRACTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "연습기록을 찾을 수 없습니다."),
    ANALYSIS_NOT_FOUND(HttpStatus.NOT_FOUND, "분석결과를 찾을 수 없습니다."),

    /*
     * 405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),

    /*
     * 409 CONFLICT
     */
    USERNAME_CONFLICT(HttpStatus.CONFLICT, "존재하는 회원 이름입니다."),
    PROJECT_CONFLICT(HttpStatus.CONFLICT, "해당 회원에게 이미 존재하는 프로젝트 이름입니다."),
    MODEL_CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 모델명 입니다."),

    /*
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),

    ;

    private final HttpStatus status;
    private final String message;

}
