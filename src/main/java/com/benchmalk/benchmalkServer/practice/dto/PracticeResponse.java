package com.benchmalk.benchmalkServer.practice.dto;

import com.benchmalk.benchmalkServer.clova.dto.AnalysisResponse;
import com.benchmalk.benchmalkServer.practice.domain.Practice;
import com.benchmalk.benchmalkServer.practice.domain.PracticeStatus;
import com.benchmalk.benchmalkServer.project.dto.ProjectResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PracticeResponse {

    private Long id;
    private String name;
    private String memo;
    private PracticeStatus status;
    private LocalDateTime created_date;
    private ProjectResponse project;
    private AnalysisResponse analysis;


    public PracticeResponse(Practice practice) {
        this.id = practice.getId();
        this.name = practice.getName();
        this.memo = practice.getMemo();
        this.status = practice.getStatus();
        this.created_date = LocalDateTime.now();
        this.project = new ProjectResponse(practice.getProject());
        if (practice.getClovaAnalysis() != null) {
            this.analysis = new AnalysisResponse(practice.getClovaAnalysis());
        }
    }
}
