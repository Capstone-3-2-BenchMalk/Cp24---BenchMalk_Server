package com.benchmalk.benchmalkServer.clova.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ClovaResponse {
    private String result;
    private String message;
    private String token;
    private String version;
    private List<ClovaResponseSegments> segments;
    private String text;
    private Double confidence;
}
