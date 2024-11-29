package com.benchmalk.benchmalkServer.project.domain;

import com.benchmalk.benchmalkServer.model.domain.Model;
import com.benchmalk.benchmalkServer.practice.domain.Practice;
import com.benchmalk.benchmalkServer.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private LocalDateTime created_date = LocalDateTime.now();

    @NotNull
    private Integer minTime;

    @NotNull
    private Integer maxTime;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne(cascade = C)
    private Model model;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Practice> practices;

    public Project(String name, Integer minTime, Integer maxTime, User user) {
        this.name = name;
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.user = user;
    }
}
