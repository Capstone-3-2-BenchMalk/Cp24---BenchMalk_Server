package com.benchmalk.benchmalkServer.user.domain;

import com.benchmalk.benchmalkServer.model.domain.Model;
import com.benchmalk.benchmalkServer.project.domain.Project;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String userid;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    @JsonIgnore
    @NotNull
    private LocalDateTime createdDate = LocalDateTime.now();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Model> addedModels;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Project> projects;

    public User(String userid, String username, String password) {
        this.userid = userid;
        this.username = username;
        this.password = password;
    }
}
