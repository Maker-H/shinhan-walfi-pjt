package com.shinhan.walfi.service.banking;

import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.domain.banking.Account;
import com.shinhan.walfi.domain.banking.ExchangeHistory;
import com.shinhan.walfi.dto.banking.ExchangeDto;
import com.shinhan.walfi.exception.*;
import com.shinhan.walfi.mapper.BankMapper;
import com.shinhan.walfi.repository.UserRepository;
import com.shinhan.walfi.repository.banking.AccountRepository;
import com.shinhan.walfi.util.AccountUtil;
import com.shinhan.walfi.util.DateConversionUtil;
import com.shinhan.walfi.util.ExchangeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final AccountRepository accountRepository;

    private final DateConversionUtil dateConversionUtil;

    private final UserRepository userRepository;

    private final ExchangeUtil exchangeUtil;

    private final AccountUtil accountUtil;

    private final BankMapper bankMapper;

    /**
     * 정기적금 상품 가입
     *
     * @exception 'NO_MATCHING_USER' - 비밀번호가 틀리거나 존재하지 않는 사용자의 경우 예외 발생
     * @exception 'CANNOT_FIND_ACCOUNT' - 세부 계좌를 찾을 수 없는 경우 예외 발생
     * @param userId
     * @param 통화코드
     * @param 상품명
     * @param 금리
     */
    @Override
    public void createLevelUpTimeDeposit(String userId,
                                  String mainAccountNum,
                                  String 통화코드,
                                  String 상품명,
                                  BigDecimal 금리,
                                  long 입금금액) {

        User user = userRepository.find(userId);
        if (user == null) {
            log.error("=== id: " + user.getUserId() + " 틀린 비밀번호이거나 존재하지 않는 회원 ===");
            throw new UserException(UserErrorCode.NO_MATCHING_USER);
        }

        // 만기일 계산
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Date 만기일 = calendar.getTime();

        String targetAccountNum = accountRepository.find저축예금AccountNum(mainAccountNum, 통화코드);

        if (targetAccountNum == null) {
            throw new AccountException(AccountErrorCode.CANNOT_FIND_ACCOUNT);
        }

        // 잔액 확인 후 부족하면 예외 발생
        Account targetAccount = accountRepository.findAccount(targetAccountNum);

        if (targetAccount.get잔액통화별() < 입금금액) {
            log.error("=== id: " + userId + "의 계좌 잔액이 부족합니다");
            throw new TransferException(TransferErrorCode.OVERDRAWN);
        }

//        List<String> accounts = accountRepository.findAccountsWithOnlyAccountNum();
        String accountNum = accountUtil.createAccountNum();

        // 상품 만들기
        Account productAccount = null;
        if (통화코드.equals("KRW")) {
            productAccount = Account.createKrwProductAccount(accountNum, 상품명, 만기일, 금리, 통화코드, user, 입금금액);
            // 사용자가 가진 통화계좌에서 출금
            bankMapper.withdrawTransferMoneyFromAccount(targetAccountNum, 입금금액);
        } else {
            long 원화환산금액 = 0;
            List<ExchangeHistory> todayExchanges;

            try {
                todayExchanges = exchangeUtil.getTodayExchange();
            } catch (org.json.simple.parser.ParseException e) {
                throw new RuntimeException(e);
            }

            for (ExchangeHistory todayExchange : todayExchanges) {
                if (todayExchange.get통화코드().equals(통화코드)) {
                    원화환산금액 = (long) (Math.floor(todayExchange.get매매기준환율() * 입금금액));
                }
            }

            productAccount = Account.createGlobalProductAccount(accountNum, 상품명, 만기일, 금리, 통화코드, user, 입금금액, 원화환산금액);
            // 사용자가 가진 통화계좌에서 출금
            bankMapper.globalWithdrawTransferMoneyFromAccount(targetAccountNum, 입금금액, 원화환산금액);

        }
        accountRepository.save(productAccount);




        log.info("=== " + 상품명 + " " + 통화코드 + " " + " 생성 ===");

    }
}
