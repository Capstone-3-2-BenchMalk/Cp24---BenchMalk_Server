package com.benchmalk.benchmalkServer.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectModifyRequest {
    @NotNull
    private Long projectid;
    private String name;
    private Integer min_time;
    private Integer max_time;
    private Long modelid;
}
