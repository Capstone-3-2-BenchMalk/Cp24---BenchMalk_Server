package com.benchmalk.benchmalkServer.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectRequest {
    @NotNull
    private String userid;
    @NotNull
    private String name;
    @NotNull
    private Integer min_time;
    @NotNull
    private Integer max_time;
}
