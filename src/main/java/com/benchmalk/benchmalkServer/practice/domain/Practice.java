package com.benchmalk.benchmalkServer.practice.domain;

import com.benchmalk.benchmalkServer.project.domain.Project;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
    private LocalDateTime createdDate = LocalDateTime.now();

    @NotNull
    private PracticeStatus status = PracticeStatus.CREATED;

    private String memo;

    @ManyToOne
    @NotNull
    private Project project;

    @OneToOne
    private PracticeAnalysis practiceAnalysis;

    public Practice(String name, String memo, Project project) {
        this.name = name;
        this.project = project;
        this.memo = memo;
    }
}
