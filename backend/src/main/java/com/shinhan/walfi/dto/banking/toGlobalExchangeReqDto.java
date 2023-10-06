package com.shinhan.walfi.dto.banking;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class toGlobalExchangeReqDto {

    private String 사용자대표계좌;

    private String 도착계좌통화코드;

    private long 금액;

    private float 전신환매도환율;

}
