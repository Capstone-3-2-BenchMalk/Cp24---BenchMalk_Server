package com.benchmalk.benchmalkServer.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginResponse {

    private String token;
}