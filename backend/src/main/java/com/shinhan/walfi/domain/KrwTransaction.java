package com.shinhan.walfi.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Time;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class KrwTransaction {

    @Id
    private Long 원화거래번호;

    private Date 거래일자;

    private Time 거래시각;

    private String 적요;

    private Long 출금금액;

    private Long 입금금액;

    private String 내용;

    private Long 잔액;

    private Byte 입지구분;

    private String 거래점명;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "계좌번호")
    private Account account;

}
