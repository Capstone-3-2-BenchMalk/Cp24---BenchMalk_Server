package com.benchmalk.benchmalkServer.model.dto;

import com.benchmalk.benchmalkServer.model.domain.Model;
import com.benchmalk.benchmalkServer.model.domain.ModelType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModelResponse {
    private Long id;
    private String name;
    private ModelType type;
    private String userid;

    public ModelResponse(Model model) {
        this.id = model.getId();
        this.name = model.getName();
        this.type = model.getModelType();
        if (model.getUser() != null) {
            this.userid = model.getUser().getUserid();
        }
    }
}
