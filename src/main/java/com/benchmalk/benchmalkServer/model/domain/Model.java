package com.benchmalk.benchmalkServer.model.domain;

import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import com.benchmalk.benchmalkServer.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ModelType modelType;

    @NotNull
    private LocalDateTime createdDate = LocalDateTime.now();

    @ManyToOne
    private User user;

    @OneToOne
    private ModelAnalysis modelAnalysis;

    public Model(String name, ModelType modelType) {
        if (modelType != ModelType.PROVIDED) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        this.name = name;
        this.modelType = modelType;
    }

    public Model(String name, ModelType modelType, User user) {
        if (modelType != ModelType.CREATED) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        this.name = name;
        this.modelType = modelType;
        this.user = user;
    }
}
