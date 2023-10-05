package com.shinhan.walfi.repository.banking;

import com.shinhan.walfi.domain.banking.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {
    @Query(value = "select * from account where 계좌번호 = ?1", nativeQuery = true)
    Account findAccount(String account);

    @Query(value = "select 계좌번호 from account", nativeQuery = true)
    List<String> findAccountsWithOnlyAccountNum();

    @Query(value = "select 계좌번호 from account where 상품명 = '저축예금' and 통화 = ?2 and 대표계좌 = ?1", nativeQuery = true)
    String find저축예금AccountNum(String mainAccount, String 통화);

    @Query(value = "select * from account where 상품명 != '저축예금' and 만기일 = ?1", nativeQuery = true)
    List<Account> findAllProductAccount(Date expireDate);

    @Query(value = "select 계좌번호 from account order by 계좌번호 desc limit 1;", nativeQuery = true)
    String findLastAccountNum();
}
