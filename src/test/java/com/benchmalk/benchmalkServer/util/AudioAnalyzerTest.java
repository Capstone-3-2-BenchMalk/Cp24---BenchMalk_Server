package com.benchmalk.benchmalkServer.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

class AudioAnalyzerTest {

    @Test
    void test() {
        AudioAnalyzer a = new AudioAnalyzer();
        Assertions.assertThatCode(() ->
                System.out.println(a.analyzePitch("src/test/resources/test.mp3").toString())).doesNotThrowAnyException();
    }

    @Test
    void test2() throws UnsupportedAudioFileException, IOException {
        AudioAnalyzer a = new AudioAnalyzer();
        Assertions.assertThatCode(() -> {
            String audioFilePath = "src/test/resources/test.mp3";
            System.out.println(a.getDuration(audioFilePath));
        }).doesNotThrowAnyException();
    }

    @Test
    void test3() throws UnsupportedAudioFileException, IOException {
        AudioAnalyzer a = new AudioAnalyzer();
        Assertions.assertThatCode(() ->
                System.out.println(a.analyzeVolume("src/test/resources/test.mp3").toString())).doesNotThrowAnyException();
    }

}