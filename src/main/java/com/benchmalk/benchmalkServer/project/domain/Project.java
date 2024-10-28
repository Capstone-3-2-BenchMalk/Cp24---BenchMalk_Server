package com.benchmalk.benchmalkServer.project.domain;

import com.benchmalk.benchmalkServer.model.domain.Model;
import com.benchmalk.benchmalkServer.practice.domain.Practice;
import com.benchmalk.benchmalkServer.user.domain.User;
import jakarta.persistence.*;
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
    @Column(unique = true)
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
    @NotNull
    private Model model;

    @OneToMany(mappedBy = "project")
    private List<Practice> practices;

    public Project(Model model, User user, String name, Long id) {
        this.model = model;
        this.user = user;
        this.name = name;
    }
}
