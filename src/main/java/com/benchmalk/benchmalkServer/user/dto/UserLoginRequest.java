package com.benchmalk.benchmalkServer.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest {
    @NotBlank
    private String userid;
    @NotBlank
    private String password;
}
