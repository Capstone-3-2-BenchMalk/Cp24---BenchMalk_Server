package com.benchmalk.benchmalkServer.clova;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
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

@RestController
@Slf4j
@RequestMapping("/clova/test")
@Getter
public class APITEST {
    private final String INVOKE_URL = "https://clovaspeech-gw.ncloud.com/external/v1/9454/4e758d4760a6cffc347cdb45f0966d20f481bad806731c4c0e44f21cf9d90bb5";
    private final String secret = "d4be9f1733bb43d7811f4fa94fc34f74";
    private WebClient webClient;

    public APITEST() {


        this.webClient = WebClient.builder()
                .baseUrl(INVOKE_URL)
                .defaultHeaders(httpheaders -> {
                    httpheaders.add("X-CLOVASPEECH-API-KEY", secret);
                })
                .build();
    }

    @GetMapping("/request")
    public void request() {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        File file = new File("src/files/test.mp4");
        builder.part("media", new FileSystemResource(file));
        builder.part("params", APIparams.builder().language("enko").completion("sync").build());
        try {
            String monoResponse = webClient.post()
                    .uri("/recognizer/upload")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(builder.build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println(monoResponse);
        } catch (WebClientResponseException e) {
            System.out.println(e.getResponseBodyAsString());
            System.out.println(e);
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
            builder.part("params", APIparams.builder().language("enko").completion("async").callback("http://144.24.94.110:8080//clova/test/getC").build());
            Mono<String> monoResponse = webClient.mutate().build().post()
                    .uri("/recognizer/upload")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(builder.build())
                    .retrieve()
                    .bodyToMono(String.class);
            monoResponse.subscribe(this::print);
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
