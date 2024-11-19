package com.benchmalk.benchmalkServer.clova.service;

import com.benchmalk.benchmalkServer.clova.domain.ClovaAnalysis;
import com.benchmalk.benchmalkServer.clova.dto.ClovaRequest;
import com.benchmalk.benchmalkServer.clova.dto.ClovaResponse;
import com.benchmalk.benchmalkServer.clova.repository.ClovaAnalysisRepository;
import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
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

@Service
@RequiredArgsConstructor
public class ClovaService {
    private final ClovaAnalysisRepository clovaAnalysisRepository;
    private final ClovaParser clovaParser;
    private final ScoreCalculator scoreCalculator;

    private final String INVOKE_URL = "https://clovaspeech-gw.ncloud.com/external/v1/9572/27facad300bf3a3cb0613afaa165db17b0e16a6c8d7a992f6b3bc4ec46912f56";
    private final String secret = "c49bca2a826f408bb91fb17da054f6c5";
    private WebClient webClient;


    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(INVOKE_URL)
                .defaultHeaders(httpheaders -> {
                    httpheaders.add("X-CLOVASPEECH-API-KEY", secret);
                })
                .build();
    }

    public Mono<ClovaResponse> callClova(String filePath) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            File file = new File(filePath);
            builder.part("media", new FileSystemResource(file));
            builder.part("params", ClovaRequest.builder().language("enko").completion("sync").build());
            Mono<ClovaResponse> monoResponse = webClient.post()
                    .uri("/recognizer/upload")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(builder.build())
                    .retrieve()
                    .bodyToMono(ClovaResponse.class);
            return monoResponse;
        } catch (WebClientResponseException e) {
            System.out.println(e.getResponseBodyAsString());
            System.out.println(e.getStatusCode() + e.getStatusText());
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public ClovaAnalysis createAnalysis(ClovaResponse clovaResponse) {
        ClovaAnalysis analysis = clovaParser.parse(clovaResponse);
        analysis.setWpm(scoreCalculator.calculateAnalysisWPM(analysis));
        analysis.setRest(scoreCalculator.calculateAnalysisRest(analysis));
        analysis.getSentences().forEach(s ->
                s.setWpm(scoreCalculator.calculateSentenceWPM(s)));
        clovaAnalysisRepository.save(analysis);
        return analysis;
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
