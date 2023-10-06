package com.shinhan.walfi.dto.transfer;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;


@Getter
@ToString
public class TransferDTO {

    private String 출금계좌번호;

    private String 입금은행코드;

    private BigDecimal 이체금액;

    private String 통화코드;

    private String 입금계좌번호;

    private String 입금계좌통장메모;

    private String 출금계좌통장메모;
}
