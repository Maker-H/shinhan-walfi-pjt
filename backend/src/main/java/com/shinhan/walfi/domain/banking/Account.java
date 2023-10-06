package com.shinhan.walfi.domain.banking;

import com.shinhan.walfi.domain.User;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    private String 계좌번호;

    private String 구분;

    private String 상품명;

    @Column(name = "`잔액(통화별)`")
    private long 잔액통화별;

    @Column(name = "`평가금액(통화별)`")
    private long 평가금액통화별;

    private Date 신규일;

    private Date 만기일;

    private String 관리점명;

    @Column(name = "`금리(수익률)`", precision = 4, scale = 2)
    private BigDecimal 금리수익률;

    private String 통화;

    private String 과세;

    @Column(name = "`잔액(원화)`")
    private long 잔액원화;

    @Column(name = "`평가금액(원화)`")
    private long 평가금액원화;

    private byte 자동해지여부;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "`대표계좌`", referencedColumnName = "`대표계좌`")
    private User user;

    @OneToMany(mappedBy = "account")
    private List<KrwTransaction> krwTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<GlobalTransaction> globalTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<KrwAccountTransaction> krwAccountTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<GlobalAccountTransaction> globalAccountTransactions = new ArrayList<>();

    public static Account createKrwProductAccount(
                              String 계좌번호,
                              String 상품명,
                              Date 만기일,
                              BigDecimal 금리수익률,
                              String 통화,
                              User user,
                              long 입금금액) {

        Account account = new Account();

        account.계좌번호 = 계좌번호;

        account.구분 = "예적금";
        account.관리점명 = "영업부";
        account.통화 = 통화;
        account.과세 = "일반과세";
        account.잔액원화 = 입금금액;
        account.평가금액원화 = 입금금액;

        account.상품명 = 상품명;
        account.만기일 = 만기일;
        account.금리수익률 = 금리수익률;
        account.자동해지여부 = 0;

        account.잔액통화별 = 입금금액;
        account.평가금액통화별 = 입금금액;

        Date now = new Date();
        account.신규일 = now;

        account.user = user;
        user.getAccounts().add(account);

        return account;

    }

    public static Account createGlobalProductAccount(
            String 계좌번호,
            String 상품명,
            Date 만기일,
            BigDecimal 금리수익률,
            String 통화,
            User user,
            long 입금금액,
            long 원화환산금액) {

        Account account = new Account();

        account.계좌번호 = 계좌번호;

        account.구분 = "예적금";
        account.관리점명 = "영업부";
        account.통화 = 통화;
        account.과세 = "일반과세";


        account.상품명 = 상품명;
        account.만기일 = 만기일;
        account.금리수익률 = 금리수익률;
        account.자동해지여부 = 0;

        account.잔액통화별 = 입금금액;
        account.평가금액통화별 = 입금금액;
        account.잔액원화 = 원화환산금액;
        account.평가금액원화 = 원화환산금액;

        Date now = new Date();
        account.신규일 = now;

        account.user = user;
        user.getAccounts().add(account);

        return account;

    }

    public static Account createBasicAccount(String 계좌번호, String 통화, User user) {
        Account account = new Account();

        account.계좌번호 = 계좌번호;

        account.구분 = "예적금";
        account.잔액통화별 = 0;
        account.평가금액통화별 = 0;
        account.관리점명 = "영업부";
        account.통화 = 통화;
        account.과세 = "일반과세";
        account.잔액원화 = 0;
        account.평가금액원화 = 0;

        account.상품명 = "저축예금";
        account.금리수익률 = BigDecimal.valueOf(0.00);
        account.자동해지여부 = 0;

        Date now = new Date();
        account.신규일 = now;

        account.user = user;
        user.getAccounts().add(account);

        return account;

    }
}
