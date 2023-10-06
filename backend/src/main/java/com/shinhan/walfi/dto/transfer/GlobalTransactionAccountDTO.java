package com.shinhan.walfi.dto.transfer;

import lombok.Builder;
import lombok.ToString;


@ToString
@Builder
public class GlobalTransactionAccountDTO {

    private String 출금계좌번호;

    private String 입금계좌번호;

    private String 통화코드;

    private long 외화금액;

    private long 거래후잔액;

    private String 계좌번호;
}
