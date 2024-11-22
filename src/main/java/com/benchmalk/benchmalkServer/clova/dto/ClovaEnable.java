package com.benchmalk.benchmalkServer.clova.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClovaEnable {
    private Boolean enable;

    public ClovaEnable(Boolean enable) {
        this.enable = enable;
    }
}
