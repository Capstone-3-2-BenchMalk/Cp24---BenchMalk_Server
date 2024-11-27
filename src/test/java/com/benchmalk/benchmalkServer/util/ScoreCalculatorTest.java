package com.benchmalk.benchmalkServer.util;

import org.junit.jupiter.api.Test;

class ScoreCalculatorTest {
    private final ScoreCalculator calculator = new ScoreCalculator(new AudioAnalyzer());

    @Test
    void test() {
        System.out.println(calculator.calculateEnergy("src/test/resources/220.mp3"));
    }

}