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
                System.out.println(calculator.calculateAccent(
                        analyzer.analyzePitch("src/test/resources/byung.mp3"),
                        analyzer.analyzeVolume("src/test/resources/byung.mp3"))
                )
        ).doesNotThrowAnyException();
    }

    @Test
    void test2() {
        Assertions.assertThatCode(() -> {
                    System.out.println(calculator.calculateSD(
                            analyzer.analyzePitch("src/test/resources/bigPitchSd.mp3"))
                    );
                    System.out.println(calculator.calculateSD(
                            analyzer.analyzePitch("src/test/resources/smallPitchSd.mp3"))
                    );
                }
        ).doesNotThrowAnyException();
    }


}