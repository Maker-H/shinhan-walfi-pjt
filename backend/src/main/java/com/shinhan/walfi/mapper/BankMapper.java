package com.shinhan.walfi.mapper;

import com.shinhan.walfi.dto.TestEntityDTO;
import com.shinhan.walfi.dto.transfer.KRWTransactionAccountDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface BankMapper {

    /**
     * =============================
     * ===== 이체 관련 Method ======
     * =============================
     */

    // 대표 계좌 번호 조회
    boolean findMainAccountNumber(String mainWithdrawalAccountNumber);

    // 세부 계좌 번호 통화 코드로 조회
    String findSubAccountNumberByCurrencyCode(String mainWithdrawalAccountNumber, String currencyCode);

    // 출금 계좌에 이체 금액 이상의 돈이 있는지 확인
    boolean checkSufficientMoneyForTransfer(@Param("subAccountNumber") String subAccountNumber,
                                            @Param("transferMoney") long transferMoney);

    // 출금 계좌에 이체 금액만큼 차감
    int withdrawTransferMoneyFromAccount(@Param("accountNumber") String accountNumber,
                                         @Param("transferMoney") long transferMoney);

    // 입금 계좌에 이체 금액만큼 입금
    int depositTransferMoneyFromAccount(@Param("accountNumber") String accountNumber,
                                        @Param("transferMoney") long transferMoney);

    void saveAccountTransaction(KRWTransactionAccountDTO krwTransactionAccountDTO);

    void testAddData();

    TestEntityDTO testLoadData();

    int globalDepositTransferMoneyFromAccount(@Param("accountNumber") String accountNumber,
                                        @Param("transferMoney") long transferMoney,
                                              @Param("krwConvertedMoney") long krwConvertedMoney);

    int globalWithdrawTransferMoneyFromAccount(@Param("accountNumber") String accountNumber,
                                              @Param("transferMoney") long transferMoney,
                                              @Param("krwConvertedMoney") long krwConvertedMoney);

}
