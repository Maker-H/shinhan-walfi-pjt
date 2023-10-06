package com.shinhan.walfi.dto.transfer;

import lombok.Builder;
import lombok.ToString;

import java.util.Objects;


@ToString
@Builder
public class KRWTransactionAccountDTO {

    private String 계좌번호;

    private String 상대계좌번호;

    private String 입금은행코드;

    private String 이체종류;

    private long 이체금액;

    private long 거래후잔액;

    private String 입금계좌통장메모;

    private String 출금계좌통장메모;

    public KRWTransactionAccountDTO revert(long 거래후잔액) {

        String tmp = 계좌번호;
        this.계좌번호 = 상대계좌번호;
        this.상대계좌번호 = tmp;

        this.이체종류 = Objects.equals(this.이체종류, "입금") ? "출금" : "입금";

        this.거래후잔액 = 거래후잔액;

        return this;
    }
}
