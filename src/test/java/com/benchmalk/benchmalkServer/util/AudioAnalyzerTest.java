package com.benchmalk.benchmalkServer.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
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
        Assertions.assertThatCode(() -> {
            String audioFilePath = "src/test/resources/test.mp3";
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(audioFilePath));
            AudioFormat format = ais.getFormat();
            System.out.println(format.toString());
        }).doesNotThrowAnyException();

    }

}