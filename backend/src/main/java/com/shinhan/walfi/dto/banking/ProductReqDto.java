package com.shinhan.walfi.dto.banking;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class ProductReqDto {

    private String mainAccountNum;

    private Long 입금금액;

    private String 통화코드;

    private String 상품명;

    private String 만기일;

    private BigDecimal 금리;

    @JsonSetter("금리")
    public void set금리(String 금리) {
        BigDecimal 변환금리 = new BigDecimal(금리);
        this.금리 = 변환금리;
    }

}
