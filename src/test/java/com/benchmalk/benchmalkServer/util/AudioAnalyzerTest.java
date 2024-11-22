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
                a.analyzePitch("src/test/resources/test.mp3")).doesNotThrowAnyException();
    }

    @Test
    void test2() throws UnsupportedAudioFileException, IOException {
        AudioAnalyzer a = new AudioAnalyzer();
        Assertions.assertThatCode(() -> {
            String audioFilePath = "src/test/resources/test.mp3";
            System.out.println(a.getDuration(audioFilePath));
        }).doesNotThrowAnyException();

    }

}