package com.benchmalk.benchmalkServer.project.dto;

import com.benchmalk.benchmalkServer.project.domain.Project;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectResponse {
    private Long id;
    private String userid;
    private String name;

    public ProjectResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.userid = project.getUser().getUserid();
    }
}
