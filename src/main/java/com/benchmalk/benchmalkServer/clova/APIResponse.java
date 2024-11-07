package com.benchmalk.benchmalkServer.clova;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Array;

@Getter
@Setter
@Builder
public class APIResponse {
    private String result;
    private String message;
    private String token;
    private String version;
//    private Object params;
    private Integer progress;
    private Array segments;
    private String text;
    private Double confidence;
    private Array speakers;
    private Array events;
    private Array eventTypes;
}
