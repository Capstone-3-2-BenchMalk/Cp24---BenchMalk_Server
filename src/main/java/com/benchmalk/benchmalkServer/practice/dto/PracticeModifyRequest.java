package com.benchmalk.benchmalkServer.practice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PracticeModifyRequest {
    @NotNull
    private Long practiceid;
    private String name;
    private String memo;
}
