package com.benchmalk.benchmalkServer.util;

import com.benchmalk.benchmalkServer.clova.repository.ClovaAnalysisRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ScoreCalculatorTest {
    private final ScoreCalculator calculator = new ScoreCalculator();
    private final AudioAnalyzer analyzer = new AudioAnalyzer();
    private ClovaAnalysisRepository clovaAnalysisRepository;

    @Test
    void test() {
        Assertions.assertThatCode(() ->
                System.out.println(calculator.analyzeSD(analyzer.analyzePitch("src/test/resources/byung.mp3")))).doesNotThrowAnyException();
    }


}