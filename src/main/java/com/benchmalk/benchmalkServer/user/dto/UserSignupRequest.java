package com.benchmalk.benchmalkServer.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserSignupRequest {
    @NotBlank
    private String userid;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
