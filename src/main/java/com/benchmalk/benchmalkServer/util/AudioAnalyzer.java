package com.benchmalk.benchmalkServer.util;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class AudioAnalyzer {
    public void analyzePitch(String filePath) {

        AudioDispatcher dispatcher;
        try {
            // Increase buffer size and add overlap
            int bufferSize = 4096;
            int overlap = 1024;

            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(filePath));

            int sampleRate = (int) inputStream.getFormat().getSampleRate();

            System.out.println(inputStream.getFormat().toString());

            dispatcher = AudioDispatcherFactory.fromPipe(filePath, sampleRate, bufferSize, overlap);

            if (dispatcher == null) {
                System.out.println("Dispatcher 생성 실패");
                return;
            }

            ArrayList<Float> result = new ArrayList<>();

            PitchDetectionHandler pitchHandler = (pitchDetectionResult, audioEvent) -> {
                float pitchInHz = pitchDetectionResult.getPitch();
                System.out.println(audioEvent.getTimeStamp() + ": Detected pitch: " + pitchInHz + " Hz");
                result.add(pitchInHz);
            };

            PitchProcessor pitchProcessor = new PitchProcessor(
                    PitchProcessor.PitchEstimationAlgorithm.YIN,
                    sampleRate,
                    bufferSize,
                    pitchHandler
            );

            dispatcher.addAudioProcessor(pitchProcessor);

            dispatcher.run();

        } catch (UnsupportedAudioFileException e) {
            System.out.println("오디오 파일 형식 지원되지 않음: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("파일 읽기 실패: " + e.getMessage());
        } catch (NegativeArraySizeException e) {
            System.out.println("NegativeArraySizeException 발생: 버퍼 설정 또는 샘플 레이트 확인 필요");
            e.printStackTrace();
        }
    }
}
