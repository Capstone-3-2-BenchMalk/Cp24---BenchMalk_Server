package com.benchmalk.benchmalkServer.user.domain;

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
    private LocalDateTime created_date;

    public User(Long id, String username, String password, LocalDateTime created_date) {
        this.username = username;
        this.password = password;
        this.created_date = LocalDateTime.now();
    }
}
