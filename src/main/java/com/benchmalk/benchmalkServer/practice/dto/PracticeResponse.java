package com.benchmalk.benchmalkServer.practice.dto;

import com.benchmalk.benchmalkServer.practice.domain.Practice;
import com.benchmalk.benchmalkServer.practice.domain.PracticeStatus;
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

    public PracticeResponse(Practice practice) {
        this.id = practice.getId();
        this.name = practice.getName();
        this.memo = practice.getMemo();
        this.status = practice.getStatus();
        this.created_date = LocalDateTime.now();
    }
}
