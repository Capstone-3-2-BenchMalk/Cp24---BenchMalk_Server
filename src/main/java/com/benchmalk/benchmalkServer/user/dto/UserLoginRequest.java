package com.benchmalk.benchmalkServer.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest {
    @NotNull
    private String id;
    @NotNull
    private String password;
}
