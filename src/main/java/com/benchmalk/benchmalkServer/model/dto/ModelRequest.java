package com.benchmalk.benchmalkServer.model.dto;

import com.benchmalk.benchmalkServer.model.domain.ModelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelRequest {
    @NotBlank
    private String name;
    @NotNull
    private ModelType type;
    private String description;
}
