package com.benchmalk.benchmalkServer.model.domain;

import com.benchmalk.benchmalkServer.clova.domain.ClovaAnalysis;
import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import com.benchmalk.benchmalkServer.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ModelType modelType;

    @NotNull
    private LocalDateTime createdDate = LocalDateTime.now();

    @ManyToOne
    private User user;

    @NotBlank
    private String filepath;

    private Long duration;

    @OneToOne(cascade = CascadeType.REMOVE)
    private ClovaAnalysis clovaAnalysis;

    public Model(String name, String description, ModelType modelType, String filepath) {
        if (modelType != ModelType.PROVIDED) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        if (description.isBlank()) {
            this.description = "설명 없음";
        }
        this.name = name;
        this.modelType = modelType;
        this.filepath = filepath;
    }

    public Model(String name, String description, ModelType modelType, User user, String filepath) {
        if (modelType != ModelType.CREATED) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        if (description.isBlank()) {
            this.description = "설명 없음";
        }
        this.name = name;
        this.modelType = modelType;
        this.user = user;
        this.filepath = filepath;
    }
}
