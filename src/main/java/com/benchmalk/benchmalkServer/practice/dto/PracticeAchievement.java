package com.benchmalk.benchmalkServer.practice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PracticeAchievement {
    private float pitch;
    private float wpm;
    private float rest;
    private float confidence;

    public PracticeAchievement(Map<String, Float> data) {
        this.pitch = data.get("pitch");
        this.wpm = data.get("wpm");
        this.rest = data.get("rest");
        this.confidence = data.get("confidence");
    }
}
