package com.shinhan.walfi.dto.banking;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class ExchangeDto {

    public ExchangeDto(float 매매기준환율) {
        this.매매기준환율 = 매매기준환율;
    }

    private String 통화코드; // "USD" // cur_unit

    private String 통화명; // "미국 달러" cur_nm

    private float 전신환매입환율; // ttb

    private float 전신환매도환율; // tts

    private float 매매기준환율; //"1287.50" deal_bas_r

    private float 전일대비 = 0;

}
