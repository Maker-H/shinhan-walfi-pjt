package com.shinhan.walfi.domain.banking;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class GlobalAccountTransaction {

    @Id
    private Long 글로벌계좌거래번호;

    private String 출금계좌번호;

    private String 통화코드;

    private long 외화금액;

    private String 입금계좌번호;

    private long 거래후잔액;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "계좌번호")
    private Account account;

}
