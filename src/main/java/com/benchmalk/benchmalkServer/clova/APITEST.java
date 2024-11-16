package com.benchmalk.benchmalkServer.clova;

import com.benchmalk.benchmalkServer.clova.dto.ClovaRequest;
import com.benchmalk.benchmalkServer.clova.dto.ClovaResponse;
import com.benchmalk.benchmalkServer.util.ClovaParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.HashMap;

@Hidden
@RestController
@Slf4j
@RequestMapping("/clova/test")
@Getter
@RequiredArgsConstructor
public class APITEST {
    private final String INVOKE_URL = "https://clovaspeech-gw.ncloud.com/external/v1/9572/27facad300bf3a3cb0613afaa165db17b0e16a6c8d7a992f6b3bc4ec46912f56";
    private final String secret = "c49bca2a826f408bb91fb17da054f6c5";
    private WebClient webClient;
    private final ClovaParser clovaParser;


    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(INVOKE_URL)
                .defaultHeaders(httpheaders -> {
                    httpheaders.add("X-CLOVASPEECH-API-KEY", secret);
                })
                .build();
    }

    @GetMapping("/request")
    public void request() {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            String homePath = System.getProperty("user.home");
            File file = new File(homePath + "/benchmalk/files/test.mp3");
            builder.part("media", new FileSystemResource(file));
            builder.part("params", ClovaRequest.builder().language("enko").completion("sync").build());
            ClovaResponse monoResponse = webClient.mutate().build().post()
                    .uri("/recognizer/upload")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(builder.build())
                    .retrieve()
                    .bodyToMono(ClovaResponse.class)
                    .block();
            clovaParser.parsePractice(monoResponse);
        } catch (WebClientResponseException e) {
            System.out.println(e.getResponseBodyAsString());
            System.out.println(e.getStatusCode() + e.getStatusText());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @GetMapping("/callback")
    public void callback() {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            File file = new File("src/files/test.mp4");
            builder.part("media", new FileSystemResource(file));
            builder.part("params", ClovaRequest.builder().language("enko").completion("async").callback("http://144.24.94.110:8080//clova/test/getC").build());
            Mono<ClovaResponse> monoResponse = webClient.mutate().build().post()
                    .uri("/recognizer/upload")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(builder.build())
                    .retrieve()
                    .bodyToMono(ClovaResponse.class);
            monoResponse.subscribe(r -> {
                try {
                    System.out.println(new ObjectMapper().writeValueAsString(r));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (WebClientResponseException e) {
            System.out.println(e.getResponseBodyAsString());
            System.out.println(e);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PostMapping("/getP")
    public String print(@RequestBody String string) {
        System.out.println(string);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            HashMap<String, String> map = objectMapper.readValue(string, HashMap.class);
            Mono<String> monoResponse = webClient.mutate().build().get()
                    .uri("/recognizer/" + map.get("token"))
                    .retrieve()
                    .bodyToMono(String.class);
            monoResponse.subscribe(e -> System.out.println(e));
        } catch (WebClientResponseException e) {
            System.out.println(e.getResponseBodyAsString());
            System.out.println(e);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "post";
    }

    @PostMapping("/getC")
    public String printC(@RequestBody String string) {
        System.out.println("call post" + string);
        return "callbacked";
    }
}
