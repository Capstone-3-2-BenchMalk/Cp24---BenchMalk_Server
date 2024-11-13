package com.benchmalk.benchmalkServer.project.dto;

import com.benchmalk.benchmalkServer.project.domain.Project;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectResponse {
    private Long id;
    private String userid;
    private String name;
    private LocalDateTime created_date;
    private Integer min_time;
    private Integer max_time;

    public ProjectResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.userid = project.getUser().getUserid();
        this.min_time = project.getMin_time();
        this.max_time = project.getMax_time();
        this.created_date = project.getCreated_date();
    }
}
