package com.benchmalk.benchmalkServer.user.domain;

import com.benchmalk.benchmalkServer.model.domain.Model;
import com.benchmalk.benchmalkServer.project.domain.Project;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @NotNull
    private LocalDateTime created_date = LocalDateTime.now();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Model> added_models;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Project> projects;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
