package com.benchmalk.benchmalkServer.util;

import org.junit.jupiter.api.Test;

import java.util.List;

class ScoreCalculatorTest {
    private final ScoreCalculator calculator = new ScoreCalculator();
    private final AudioAnalyzer analyzer = new AudioAnalyzer();

    @Test
    void test() {
        List<Float> pitches = analyzer.analyzePitch("src/test/resources/my.mp3");
        List<Float> volumes = analyzer.analyzeVolume("src/test/resources/my.mp3");
        System.out.println(calculator.analyzeEnergy(pitches, volumes));
    }

}