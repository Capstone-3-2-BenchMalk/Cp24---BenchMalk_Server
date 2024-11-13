package com.benchmalk.benchmalkServer.practice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PracticeRequest {
    @NotNull
    private Long projectid;
    @NotBlank
    private String name;

    private String memo;
}
