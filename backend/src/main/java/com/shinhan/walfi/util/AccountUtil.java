package com.shinhan.walfi.util;

import com.shinhan.walfi.repository.banking.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountUtil {

    private final AccountRepository accountRepository;

    public String createAccountNum() { //List<String> accountsNum
        // Account 테이블에서 계좌 번호 조회해서 가장 뒷번호로 +1 반환
//        String defaultAccountNum = "110001";
//
//        String lastAccountNum = accountsNum.get(accountsNum.size() - 1);
//
//        Long subLastAccountNum = Long.parseLong(lastAccountNum.substring(defaultAccountNum.length())) + 1;
//
//        return defaultAccountNum + subLastAccountNum;

        String lastAccountNUm = accountRepository.findLastAccountNum();
        return lastAccountNUm;
    }
}
