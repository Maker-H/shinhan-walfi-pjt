package com.shinhan.walfi.config;

import com.shinhan.walfi.domain.banking.Account;
import com.shinhan.walfi.mapper.BankMapper;
import com.shinhan.walfi.repository.banking.AccountRepository;
import com.shinhan.walfi.util.ExchangeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AccountConfig {

    private final AccountRepository accountRepository;
    private final BankMapper bankMapper;

    @Scheduled(cron = "0 0 9 1 * *", zone = "Asia/Seoul")
    public void updateProduct() {
        // 1. 만기인 상품 이자와 원금 계산해서 대표계좌 통장으로 넣어주기
        Date currentDate = new Date();
        List<Account> productAccounts = accountRepository.findAllProductAccount(currentDate);

        for (Account productAccount : productAccounts) {
            long 잔액통화별 = productAccount.get잔액통화별();
            BigDecimal 금리수익률 = productAccount.get금리수익률();

            BigDecimal 원금 = new BigDecimal(잔액통화별);
            BigDecimal 이자 = 원금.multiply(금리수익률);

            이자 = 이자.setScale(0, RoundingMode.DOWN);

            long 원금과이자합계 = 원금.add(이자).longValue();

            String subAccountNumber = bankMapper.findSubAccountNumberByCurrencyCode(productAccount.getUser().get대표계좌(), productAccount.get통화());
            if (productAccount.get통화().equals("KRW")) {
                bankMapper.depositTransferMoneyFromAccount(subAccountNumber, 원금과이자합계);
            } else {
                //TODO: 글로벌 어카운트에 넣기
//                exchangeUtil.getTodayExchange()
//                bankMapper.globalDepositTransferMoneyFromAccount(subAccountNumber, 원금과이자합계, )
            }

        }

        // 2. 만기인 상품 통장 지우기

        // 3. 로그 찍기

    }
}
