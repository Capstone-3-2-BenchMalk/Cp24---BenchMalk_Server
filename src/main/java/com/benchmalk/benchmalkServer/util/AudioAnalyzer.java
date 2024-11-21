package com.benchmalk.benchmalkServer.util;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;
import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AudioAnalyzer {
    public List<Float> analyzePitch(String filePath) {

        AudioDispatcher dispatcher;
        try {
            // Increase buffer size and add overlap
            int bufferSize = 4096;
            int overlap = 1024;

            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(filePath));

            int sampleRate = (int) inputStream.getFormat().getSampleRate();

            System.out.println(inputStream.getFormat().toString());

            dispatcher = AudioDispatcherFactory.fromPipe(filePath, sampleRate, bufferSize, overlap);

            ArrayList<Float> result = new ArrayList<>();

            PitchDetectionHandler pitchHandler = (pitchDetectionResult, audioEvent) -> {
                float pitchInHz = pitchDetectionResult.getPitch();
//                System.out.println(audioEvent.getTimeStamp() + ": Detected pitch: " + pitchInHz + " Hz");
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

            return result;
        } catch (UnsupportedAudioFileException e) {
            System.out.println("오디오 파일 형식 지원되지 않음: " + e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            System.out.println("파일 읽기 실패: " + e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (NegativeArraySizeException e) {
            System.out.println("NegativeArraySizeException 발생: 버퍼 설정 또는 샘플 레이트 확인 필요");
            e.printStackTrace();
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Long getDuration(String filePath) {
        File file = new File(filePath);
        try {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
            long microseconds = (Long) fileFormat.properties().get("duration");
            return microseconds;
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
