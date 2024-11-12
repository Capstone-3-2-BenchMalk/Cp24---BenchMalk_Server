package com.benchmalk.benchmalkServer.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectRequest {
    @NotBlank
    private String userid;
    @NotBlank
    private String name;
    @NotBlank
    private Integer min_time;
    @NotBlank
    private Integer max_time;
}
