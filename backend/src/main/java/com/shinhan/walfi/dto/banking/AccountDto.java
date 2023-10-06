package com.shinhan.walfi.dto.banking;

import com.shinhan.walfi.domain.banking.Account;
import com.shinhan.walfi.domain.banking.CryptoWallet;
import lombok.Builder;
import lombok.Getter;
import org.bouncycastle.operator.KeyWrapper;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Builder
public class AccountDto {

    @Id
    private String 계좌번호;

    private String 구분;

    private String 상품명;

    private String 잔액통화별;

    private String 평가금액통화별;

    private Date 신규일;

    private Date 만기일;

    private String 관리점명;

    private BigDecimal 금리수익률;

    private String 통화;

    private String 과세;

    private String 잔액원화;

    private String 평가금액원화;

    private byte 자동해지여부;


    /**
     * Account를 AccountDto로 변환하는 기능
     *
     * @param account
     * @return AccountDto
     */
    public static AccountDto accountToAccountDto(Account account) {
        return AccountDto.builder()
                .계좌번호(account.get계좌번호())
                .구분(account.get구분())
                .상품명(account.get상품명())
                .잔액통화별(String.valueOf(account.get잔액통화별()))
                .평가금액통화별(String.valueOf(account.get평가금액통화별()))
                .신규일(account.get신규일())
                .만기일(account.get만기일())
                .관리점명(account.get관리점명())
                .금리수익률(account.get금리수익률())
                .통화(account.get통화())
                .과세(account.get과세())
                .잔액원화(String.valueOf(account.get잔액원화()))
                .평가금액원화(String.valueOf(account.get평가금액원화()))
                .자동해지여부(account.get자동해지여부())
                .build();
    }

    /**
     * 가상화폐 계좌를 AccountDto로 변환하는 기능
     *
     * @param wallet, balance
     * @return AccountDto
     */
    public static AccountDto cryptoWalletToAccountDto(CryptoWallet wallet, String balance, String krwBalance) {
        return AccountDto.builder()
                .계좌번호(wallet.getAddress())
                .구분("가상화폐")
                .상품명(wallet.getCoinType().toString())
                .잔액통화별(balance)
                .평가금액통화별(balance)
                .잔액원화(krwBalance)
                .평가금액원화(krwBalance)
                .통화(wallet.getCoinType().toString())
                .build();
    }

}
