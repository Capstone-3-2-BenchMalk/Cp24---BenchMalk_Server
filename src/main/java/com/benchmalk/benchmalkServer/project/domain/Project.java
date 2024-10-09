package com.benchmalk.benchmalkServer.project.domain;

import com.benchmalk.benchmalkServer.model.domain.Model;
import com.benchmalk.benchmalkServer.user.domain.User;
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
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private LocalDateTime created_date;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Model model;

    public Project(Model model, User user, LocalDateTime created_date, String name, Long id) {
        this.model = model;
        this.user = user;
        this.created_date = LocalDateTime.now();
        this.name = name;
    }
}
