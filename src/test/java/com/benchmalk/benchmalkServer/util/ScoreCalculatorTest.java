package com.benchmalk.benchmalkServer.util;

import org.junit.jupiter.api.Test;

class ScoreCalculatorTest {
    private final ScoreCalculator calculator = new ScoreCalculator(new AudioAnalyzer());

    @Test
    void test() {
        System.out.println(calculator.analyzeEnergy("src/test/resources/220.mp3"));
    }

}