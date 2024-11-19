package com.benchmalk.benchmalkServer.util;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AudioAnalyzer {
    public void test() {
        String homePath = System.getProperty("user.home");
        String audioFilePath = homePath + "/benchmalk/files/test.mp3";

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(audioFilePath));
            JVMAudioInputStream jvmAudioStream = new JVMAudioInputStream(audioStream);

            AudioDispatcher dispatcher = new AudioDispatcher(jvmAudioStream, 1024, 0);

            PitchDetectionHandler pitchHandler = new PitchDetectionHandler() {
                @Override
                public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
                    float pitchInHz = pitchDetectionResult.getPitch();
                    System.out.println(audioEvent.getTimeStamp() + ": Detected pitch: " + pitchInHz + " Hz");
                }
            };

            PitchProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.YIN, 44100, 1024, pitchHandler);
            dispatcher.addAudioProcessor(pitchProcessor);

            dispatcher.run();

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
