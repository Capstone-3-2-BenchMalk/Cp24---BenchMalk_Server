package com.benchmalk.benchmalkServer.clova.service;

import com.benchmalk.benchmalkServer.clova.domain.ClovaAnalysis;
import com.benchmalk.benchmalkServer.clova.dto.ClovaEnable;
import com.benchmalk.benchmalkServer.clova.dto.ClovaRequest;
import com.benchmalk.benchmalkServer.clova.dto.ClovaResponse;
import com.benchmalk.benchmalkServer.clova.repository.ClovaAnalysisRepository;
import com.benchmalk.benchmalkServer.util.AudioAnalyzer;
import com.benchmalk.benchmalkServer.util.ClovaParser;
import com.benchmalk.benchmalkServer.util.ScoreCalculator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClovaService {
    private final ClovaAnalysisRepository clovaAnalysisRepository;
    private final ClovaParser clovaParser;
    private final ScoreCalculator scoreCalculator;
    private final AudioAnalyzer audioAnalyzer;

    private String INVOKE_URL;
    private String secret;
    private WebClient webClient;


    @PostConstruct
    public void init() {
        if (System.getProperty("CLOVA_URL") != null) {
            INVOKE_URL = System.getProperty("CLOVA_URL");
        } else {
            INVOKE_URL = System.getenv("CLOVA_URL");
        }
        if (System.getProperty("CLOVA_SECRET") != null) {
            secret = System.getProperty("CLOVA_SECRET");
        } else {
            secret = System.getenv("CLOVA_SECRET");
        }
        this.webClient = WebClient.builder()
                .baseUrl(INVOKE_URL)
                .defaultHeaders(httpheaders -> {
                    httpheaders.add("X-CLOVASPEECH-API-KEY", secret);
                })
                .build();
    }

    public Mono<ClovaResponse> callClova(String filePath) throws WebClientResponseException {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        File file = new File(filePath);
        builder.part("media", new FileSystemResource(file));
        builder.part("params", ClovaRequest.builder().language("enko").completion("sync").diarization(new ClovaEnable(false)).build());
        Mono<ClovaResponse> monoResponse = webClient.post()
                .uri("/recognizer/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(builder.build())
                .retrieve()
                .bodyToMono(ClovaResponse.class);
        return monoResponse;
    }

    public ClovaAnalysis createAnalysis(ClovaResponse clovaResponse, String filePath, Long duration) {
        ClovaAnalysis analysis = clovaParser.parse(clovaResponse);
        analysis.setWpm(scoreCalculator.calculateAnalysisWPM(analysis));
        Integer rest = scoreCalculator.calculateAnalysisRest(analysis);
        analysis.setRest(rest);
        analysis.setRestPerMinute((float) rest * 60 / duration);
        analysis.getSentences().forEach(s ->
                s.setWpm(scoreCalculator.calculateSentenceWPM(s)));
        List<Float> pitches = audioAnalyzer.analyzePitch(filePath);
        analysis.setPitches(pitches.toString());
        analysis.setPitchSD(scoreCalculator.calculateSD(pitches));
        List<Float> volumes = audioAnalyzer.analyzeVolume(filePath);
        analysis.setVolumes(volumes.toString());
        analysis.setVolumeSD(scoreCalculator.calculateSD(volumes));
        analysis.setPitch(scoreCalculator.getMean(pitches));
        analysis.setAccent((float) scoreCalculator.calculateAccent(pitches, volumes) * 60 / duration);
        clovaAnalysisRepository.save(analysis);
        return analysis;
    }


    public Map<String, Float> calculateAchievement(ClovaAnalysis target, ClovaAnalysis criteria) {
        if (target == null || criteria == null) {
            return Map.of("pitch", -1F, "wpm", -1F, "rest", -1F, "confidence", -1F,
                    "pitchSD", -1F, "volumeSD", -1F, "accent", -1F);
        }
        Map<String, Float> achievements = new HashMap<>();
        float pitchAchievement = getAchieve((float) target.getPitch(), (float) criteria.getPitch());
        float wpmAchievement = getAchieve((float) target.getWpm(), (float) criteria.getWpm());
        float restAchievement = getAchieve(target.getRestPerMinute(), criteria.getRestPerMinute());
        float confidenceAchievement = getAchieve(target.getConfidence().floatValue(), criteria.getConfidence().floatValue());
        float pitchSDAchievement = getAchieve(target.getPitchSD(), criteria.getPitchSD());
        float volumeSDAchievement = getAchieve(target.getVolumeSD(), criteria.getVolumeSD());
        float accentAchievement = getAchieve(target.getAccent(), criteria.getAccent());
        achievements.put("pitch", pitchAchievement);
        achievements.put("wpm", wpmAchievement);
        achievements.put("rest", restAchievement);
        achievements.put("confidence", confidenceAchievement);
        achievements.put("pitchSD", pitchSDAchievement);
        achievements.put("volumeSD", volumeSDAchievement);
        achievements.put("accent", accentAchievement);
        return achievements;
    }

    private float getAchieve(float target, float criteria) {
        float result = (target / criteria) * 100;
        return result;
    }


//    public void callback() {
//        try {
//            MultipartBodyBuilder builder = new MultipartBodyBuilder();
//            File file = new File("src/files/test.mp4");
//            builder.part("media", new FileSystemResource(file));
//            builder.part("params", ClovaRequest.builder().language("enko").completion("async").callback("http://144.24.94.110:8080//clova/test/getC").build());
//            Mono<ClovaResponse> monoResponse = webClient.mutate().build().post()
//                    .uri("/recognizer/upload")
//                    .contentType(MediaType.MULTIPART_FORM_DATA)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .bodyValue(builder.build())
//                    .retrieve()
//                    .bodyToMono(ClovaResponse.class);
//            monoResponse.subscribe(r -> {
//                try {
//                    System.out.println(new ObjectMapper().writeValueAsString(r));
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        } catch (WebClientResponseException e) {
//            System.out.println(e.getResponseBodyAsString());
//            System.out.println(e);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

//    public String print(@RequestBody String string) {
//        System.out.println(string);
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            HashMap<String, String> map = objectMapper.readValue(string, HashMap.class);
//            Mono<String> monoResponse = webClient.mutate().build().get()
//                    .uri("/recognizer/" + map.get("token"))
//                    .retrieve()
//                    .bodyToMono(String.class);
//            monoResponse.subscribe(e -> System.out.println(e));
//        } catch (WebClientResponseException e) {
//            System.out.println(e.getResponseBodyAsString());
//            System.out.println(e);
//        } catch (IllegalStateException e) {
//            System.out.println(e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "post";
//    }
//
//    public String printC(@RequestBody String string) {
//        System.out.println("call post" + string);
//        return "callbacked";
//    }
}
