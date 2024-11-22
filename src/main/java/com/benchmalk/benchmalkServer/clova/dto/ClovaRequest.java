package com.benchmalk.benchmalkServer.clova.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Array;

@Setter
@Getter
@Builder
public class ClovaRequest {
    private String language;
    private String completion;
    private String callback;
    private Boolean noiseFiltering;
    private Array boostings;
    private ClovaEnable diarization;

}
