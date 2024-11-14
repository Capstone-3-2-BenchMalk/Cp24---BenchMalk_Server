package com.benchmalk.benchmalkServer.user.dto;

import org.springframework.http.HttpStatus;

public enum UserLogoutResponse {
    LOGOUT_SUCCESS("Logout Success",HttpStatus.OK),
    LOGOUT_FAILED("Logout Failed",HttpStatus.INTERNAL_SERVER_ERROR);

    private String message;
    private HttpStatus status;
    UserLogoutResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
