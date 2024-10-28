package com.benchmalk.benchmalkServer.user.domain;

import com.benchmalk.benchmalkServer.model.domain.Model;
import com.benchmalk.benchmalkServer.project.domain.Project;
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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String password;

    @NotNull
    private LocalDateTime created_date = LocalDateTime.now();

    @OneToMany(mappedBy = "user")
    private List<Model> added_models;

    @OneToMany(mappedBy = "user")
    private List<Project> projects;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
