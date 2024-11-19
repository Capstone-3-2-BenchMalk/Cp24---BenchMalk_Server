package com.benchmalk.benchmalkServer.clova.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClovaResponseSegments {
    private Long start;
    private Long end;
    private String text;
    private Double confidence;
    private List<List<?>> words;
}
