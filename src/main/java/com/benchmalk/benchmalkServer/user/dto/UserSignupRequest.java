package com.benchmalk.benchmalkServer.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserSignupRequest {
    @NotNull
    private String id;
    @NotNull
    private String username;
    @NotNull
    private String password;
}
