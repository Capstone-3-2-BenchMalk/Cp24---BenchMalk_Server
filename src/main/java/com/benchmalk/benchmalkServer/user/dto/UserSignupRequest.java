package com.benchmalk.benchmalkServer.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserSignupRequest {
    @NotNull
    private String userid;
    @NotNull
    private String username;
    @NotNull
    private String password;
}
