package com.benchmalk.benchmalkServer.practice.domain;

import com.benchmalk.benchmalkServer.project.domain.Project;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Practice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private LocalDateTime created_date;

    @NotNull
    private PracticeStatus status;

    private String memo;

    @ManyToOne
    @NotNull
    private Project project;

    @OneToOne
    private PracticeAnalysis practice_analysis;

    public Practice(Long id, String name, LocalDateTime created_date, PracticeStatus status, Project project) {
        this.name = name;
        this.created_date = LocalDateTime.now();
        this.status = status;
        this.project = project;
    }
}
