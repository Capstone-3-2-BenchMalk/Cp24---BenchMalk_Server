package com.benchmalk.benchmalkServer.clova;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Array;

@Setter
@Getter
@Builder
public class APIparams {
    private String language;
    private String completion;
    private String callback;
    private Boolean wordAlignment;
    private Boolean fullText;
    private Boolean resultToObs;
    private Boolean noiseFiltering;
    private Array boostings;
    private Boolean useDomainBoostings;
    private String forbiddens;
    private String format;
}
