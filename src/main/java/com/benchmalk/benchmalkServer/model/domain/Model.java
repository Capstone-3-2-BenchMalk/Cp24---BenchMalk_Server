package com.benchmalk.benchmalkServer.model.domain;

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
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ModelType model_type;

    @NotNull
    private LocalDateTime created_date = LocalDateTime.now();

    @ManyToOne
    private User user;

    @OneToOne
    private ModelAnalysis model_analysis;

    public Model(Long id, String name, ModelType model_type) {
        this.name = name;
        this.model_type = model_type;
    }
}
