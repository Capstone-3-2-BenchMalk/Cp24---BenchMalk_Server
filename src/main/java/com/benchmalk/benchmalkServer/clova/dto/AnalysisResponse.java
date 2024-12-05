package com.benchmalk.benchmalkServer.clova.dto;

import com.benchmalk.benchmalkServer.clova.domain.ClovaAnalysis;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalysisResponse {
    private Double Confidence;

    private Integer wpm;
    private Integer pitch;
    private Integer rest;

    private Float energy;
    private Float restPerMinute;

    private String pitches;
    private String volumes;

    public AnalysisResponse(ClovaAnalysis clovaAnalysis) {
        this.Confidence = clovaAnalysis.getConfidence();
        this.wpm = clovaAnalysis.getWpm();
        this.pitch = clovaAnalysis.getPitch();
        this.rest = clovaAnalysis.getRest();
        this.energy = clovaAnalysis.getEnergy();
        this.restPerMinute = clovaAnalysis.getRestPerMinute();
        this.pitches = clovaAnalysis.getPitches();
        this.volumes = clovaAnalysis.getVolumes();
    }
}
