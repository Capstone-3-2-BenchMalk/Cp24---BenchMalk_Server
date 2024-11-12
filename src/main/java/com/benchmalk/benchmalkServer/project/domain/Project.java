package com.benchmalk.benchmalkServer.project.domain;

import com.benchmalk.benchmalkServer.model.domain.Model;
import com.benchmalk.benchmalkServer.practice.domain.Practice;
import com.benchmalk.benchmalkServer.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @NotNull
    private String name;

    @NotNull
    private LocalDateTime created_date = LocalDateTime.now();

    @NotNull
    private Integer min_time;
    @NotNull
    private Integer max_time;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    private Model model;

    @OneToMany(mappedBy = "project")
    private List<Practice> practices;

    public Project(String name, Integer min_time, Integer max_time, User user) {
        this.name = name;
        this.min_time = min_time;
        this.max_time = max_time;
        this.user = user;
    }
}
