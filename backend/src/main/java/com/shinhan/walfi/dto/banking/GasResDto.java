package com.shinhan.walfi.dto.banking;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GasResDto {

    private String ethGas;

    private String krwGas;

    public static GasResDto getGasResDto(String ethGas, String krwGas) {
        return GasResDto.builder()
                .ethGas(ethGas)
                .krwGas(krwGas)
                .build();
    }
}
