package com.benchmalk.benchmalkServer.model.dto;

import com.benchmalk.benchmalkServer.clova.dto.AnalysisResponse;
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
    private Long duration;

    private AnalysisResponse analysis;

    public ModelResponse(Model model) {
        this.id = model.getId();
        this.name = model.getName();
        this.type = model.getModelType();
        this.duration = model.getDuration();
        if (model.getUser() != null) {
            this.userid = model.getUser().getUserid();
        }
        if (model.getClovaAnalysis() != null) {
            this.analysis = new AnalysisResponse(model.getClovaAnalysis());
        }
    }
}
