package com.benchmalk.benchmalkServer.util;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
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
    // Increase buffer size and add overlap
    int sampleBufferRatio = 5;
    int overlapRatio = 10;

    public List<Float> analyzePitch(String filePath) {

        AudioDispatcher dispatcher;
        try {


            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(filePath));

            int sampleRate = (int) inputStream.getFormat().getSampleRate();

            int bufferSize = sampleRate / sampleBufferRatio;
            int overlap = sampleRate / overlapRatio;

            System.out.println(inputStream.getFormat().toString());

            dispatcher = AudioDispatcherFactory.fromPipe(filePath, sampleRate, bufferSize, overlap);

            ArrayList<Float> result = new ArrayList<>();

            PitchDetectionHandler pitchHandler = (pitchDetectionResult, audioEvent) -> {
                float pitchInHz = pitchDetectionResult.getPitch();
                if (pitchInHz == -1) {
                    pitchInHz = 0;
                }
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
            long microseconds = (long) fileFormat.properties().get("duration");
            return (long) (microseconds / 1000000);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Float> analyzeVolume(String filePath) {
        AudioInputStream inputStream = null;
        try {
            double referenceRMS = 1.0;
            double referenceSPL = 94.0;

            inputStream = AudioSystem.getAudioInputStream(new File(filePath));

            int sampleRate = (int) inputStream.getFormat().getSampleRate();

            int bufferSize = sampleRate / sampleBufferRatio;
            int overlap = sampleRate / overlapRatio;

            AudioDispatcher dispatcher = AudioDispatcherFactory.fromPipe(filePath, sampleRate, bufferSize, overlap);

            List<Float> volumes = new ArrayList<>();

            dispatcher.addAudioProcessor(new AudioProcessor() {
                @Override
                public boolean process(AudioEvent audioEvent) {
                    float[] buffer = audioEvent.getFloatBuffer();
                    double rms = computeRMS(buffer);
                    double decibels = 20 * Math.log10(rms / referenceRMS) + referenceSPL;
                    if (Double.isNaN(decibels)) {
                        decibels = 0.0;
                    }
                    volumes.add((float) decibels);
//                    System.out.println(audioEvent.getTimeStamp() + ": Detected Volume: " + decibels + " dB");
                    return true;
                }

                @Override
                public void processingFinished() {
                    System.out.println("Volume  analysis finished");
                }

                private double computeRMS(float[] buffer) {
                    double sum = 0;
                    for (float sample : buffer) {
                        sum += sample;
                    }
                    return Math.sqrt(sum / buffer.length);
                }
            });
            dispatcher.run();

            return volumes;
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
