package com.shinhan.walfi.service.banking;

import com.shinhan.walfi.domain.banking.CryptoWallet;
import com.shinhan.walfi.domain.enums.CoinType;
import com.shinhan.walfi.dto.transfer.GlobalTransactionAccountDTO;
import com.shinhan.walfi.dto.transfer.KRWTransactionAccountDTO;
import com.shinhan.walfi.dto.transfer.TransferDTO;
import com.shinhan.walfi.exception.TransferException;
import com.shinhan.walfi.mapper.BankMapper;
import com.shinhan.walfi.repository.banking.CryptoWalletRepository;
import com.shinhan.walfi.util.CryptoUtil;
import com.shinhan.walfi.util.ExchangeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.math.BigDecimal;

import static com.shinhan.walfi.exception.TransferErrorCode.*;
import static java.util.Objects.isNull;


@Slf4j
@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final ExchangeUtil exchangeUtil;
    private final BankMapper bankMapper;
    private final CryptoUtil cryptoUtil;
    private final CryptoWalletRepository cryptoWalletRepository;


    @Transactional
    public void globalCurrencyTransfer(TransferDTO transferDTO) {

        boolean result;
        final String CURRENCY_CODE = transferDTO.get통화코드();
        log.info("통화 코드: {}", CURRENCY_CODE);

        final long TRANSFER_MONEY = transferDTO.get이체금액().longValue();
        final String DEPOSIT_MAIN_ACCOUNT_NUMBER = transferDTO.get입금계좌번호();
        final String WITHDRAWAL_MAIN_ACCOUNT_NUMBER = transferDTO.get출금계좌번호();

        if (CURRENCY_CODE.equals(CoinType.SEP.toString())) {
            CryptoWallet toWallet = cryptoWalletRepository.findWallet(DEPOSIT_MAIN_ACCOUNT_NUMBER, CoinType.SEP);
            CryptoWallet fromWallet = cryptoWalletRepository.findWallet(WITHDRAWAL_MAIN_ACCOUNT_NUMBER, CoinType.SEP);
            cryptoUtil.sendEthTransaction(fromWallet, toWallet.getAddress(), transferDTO.get이체금액());

            return;
        }

        log.debug("=== 출금 대표 계좌 번호 조회 중 ===");
        result = bankMapper.findMainAccountNumber(WITHDRAWAL_MAIN_ACCOUNT_NUMBER);
        if (!result) {
            log.error("출금 대표 계좌가 조회 불가");
            throw new TransferException(NOT_FOUND_WITHDRAWAL_MAIN_ACCOUNT_NUMBER);
        }

        log.debug("=== 입금 대표 계좌 번호 조회 중 ===");
        result = bankMapper.findMainAccountNumber(DEPOSIT_MAIN_ACCOUNT_NUMBER);
        if (!result) {
            log.error("입금 대표 계좌가 조회 불가");
            throw new TransferException(NOT_FOUND_DEPOSIT_MAIN_ACCOUNT_NUMBER);
        }

        log.debug("=== 출금 세부 계좌 번호 조회 중 ===");
        final String WITHDRAWAL_SUB_ACCOUNT_NUMBER = bankMapper.findSubAccountNumberByCurrencyCode(
                WITHDRAWAL_MAIN_ACCOUNT_NUMBER,
                CURRENCY_CODE
        );
        if (isNull(WITHDRAWAL_SUB_ACCOUNT_NUMBER)) {
            log.error("출금 세부 계좌 번호 조회 불가");
            throw new TransferException(NOT_FOUND_WITHDRAWAL_SUB_ACCOUNT_NUMBER);
        }

        log.debug("=== 입금 세부 계좌 번호 조회 중 ===");
        final String DEPOSIT_SUB_ACCOUNT_NUMBER = bankMapper.findSubAccountNumberByCurrencyCode(
                DEPOSIT_MAIN_ACCOUNT_NUMBER,
                CURRENCY_CODE
        );
        if (isNull(DEPOSIT_SUB_ACCOUNT_NUMBER)) {
            log.error("입금 세부 계좌 번호 조회 불가");
            throw new TransferException(NOT_FOUND_DEPOSIT_SUB_ACCOUNT_NUMBER);
        }

        log.debug("=== 출금 계좌에 이체 금액 이상의 돈이 있는지 확인 ===");
        result = bankMapper.checkSufficientMoneyForTransfer(
                WITHDRAWAL_SUB_ACCOUNT_NUMBER,
                TRANSFER_MONEY
        );
        if (!result) {
            log.error("잔액 부족");
            throw new TransferException(OVERDRAWN);
        }

        log.debug("{}원 송금 진행: {} -> {}", TRANSFER_MONEY, WITHDRAWAL_SUB_ACCOUNT_NUMBER, DEPOSIT_SUB_ACCOUNT_NUMBER);

        int withdrawAccountRemainMoney = bankMapper.withdrawTransferMoneyFromAccount(
                WITHDRAWAL_SUB_ACCOUNT_NUMBER,
                TRANSFER_MONEY
        );

        int depositAccountCurrentMoney = bankMapper.depositTransferMoneyFromAccount(
                DEPOSIT_SUB_ACCOUNT_NUMBER,
                TRANSFER_MONEY
        );

        log.debug("이체 보낸 사람 남은 돈: {}", withdrawAccountRemainMoney);
        log.debug("이체 받은 사람 남은 돈: {}", depositAccountCurrentMoney);
//
//        GlobalTransactionAccountDTO globalTransactionAccountDTO = GlobalTransactionAccountDTO.builder()
//                .출금계좌번호(WITHDRAWAL_MAIN_ACCOUNT_NUMBER)
//                .입금계좌번호(DEPOSIT_MAIN_ACCOUNT_NUMBER)
//                .통화코드(CURRENCY_CODE)
//                .외화금액()
//                .거래후잔액()
//                .계좌번호()
//                .build();
    }

    /**
     * 원화 이체 Logic
     * <p>
     * 1. 출금 계좌 번호 조회 <p>
     * 2. 입금 대표 계좌 번호 조회 <p>
     * 3. 입금 세부 계좌 번호 조회 <p>
     * 4. 출금 계좌에 이체 금액 이상의 돈이 있는지 확인 <p>
     * 5. 출금 계좌에 이체 금액만큼 차감 <p>
     * 6. 입금 계좌에 이체 금액만큼 입금 <p>
     *
     * @param transferDTO
     */
    @Override
    @Transactional
    public void localCurrencyTransfer(TransferDTO transferDTO) {

        boolean result;
        final String CURRENCY_CODE = "KRW";

        final long TRANSFER_MONEY = transferDTO.get이체금액().longValue();
        final String DEPOSIT_MAIN_ACCOUNT_NUMBER = transferDTO.get입금계좌번호();
        final String WITHDRAWAL_MAIN_ACCOUNT_NUMBER = transferDTO.get출금계좌번호();

        log.debug("=== 출금 대표 계좌 번호 조회 중 ===");
        result = bankMapper.findMainAccountNumber(WITHDRAWAL_MAIN_ACCOUNT_NUMBER);
        if (!result) {
            log.error("출금 대표 계좌가 조회 불가");
            throw new TransferException(NOT_FOUND_WITHDRAWAL_MAIN_ACCOUNT_NUMBER);
        }

        log.debug("=== 입금 대표 계좌 번호 조회 중 ===");
        result = bankMapper.findMainAccountNumber(DEPOSIT_MAIN_ACCOUNT_NUMBER);
        if (!result) {
            log.error("입금 대표 계좌가 조회 불가");
            throw new TransferException(NOT_FOUND_DEPOSIT_MAIN_ACCOUNT_NUMBER);
        }

        log.debug("=== 출금 세부 계좌 번호 조회 중 ===");
        final String WITHDRAWAL_SUB_ACCOUNT_NUMBER = bankMapper.findSubAccountNumberByCurrencyCode(
                WITHDRAWAL_MAIN_ACCOUNT_NUMBER,
                CURRENCY_CODE
        );
        if (isNull(WITHDRAWAL_SUB_ACCOUNT_NUMBER)) {
            log.error("출금 세부 계좌 번호 조회 불가");
            throw new TransferException(NOT_FOUND_WITHDRAWAL_SUB_ACCOUNT_NUMBER);
        }


        log.debug("=== 입금 세부 계좌 번호 조회 중 ===");
        final String DEPOSIT_SUB_ACCOUNT_NUMBER = bankMapper.findSubAccountNumberByCurrencyCode(
                DEPOSIT_MAIN_ACCOUNT_NUMBER,
                CURRENCY_CODE
        );
        if (isNull(DEPOSIT_SUB_ACCOUNT_NUMBER)) {
            log.error("입금 세부 계좌 번호 조회 불가");
            throw new TransferException(NOT_FOUND_DEPOSIT_SUB_ACCOUNT_NUMBER);
        }

        log.debug("=== 출금 계좌에 이체 금액 이상의 돈이 있는지 확인 ===");
        result = bankMapper.checkSufficientMoneyForTransfer(
                WITHDRAWAL_SUB_ACCOUNT_NUMBER,
                TRANSFER_MONEY
        );
        if (!result) {
            log.error("잔액 부족");
            throw new TransferException(OVERDRAWN);
        }

        log.debug("{}원 송금 진행: {} -> {}", TRANSFER_MONEY, WITHDRAWAL_SUB_ACCOUNT_NUMBER, DEPOSIT_SUB_ACCOUNT_NUMBER);

        int withdrawAccountRemainMoney = bankMapper.withdrawTransferMoneyFromAccount(
                WITHDRAWAL_SUB_ACCOUNT_NUMBER,
                TRANSFER_MONEY
        );

        int depositAccountCurrentMoney = bankMapper.depositTransferMoneyFromAccount(
                DEPOSIT_SUB_ACCOUNT_NUMBER,
                TRANSFER_MONEY
        );

        log.debug("이체 보낸 사람 남은 돈: {}", withdrawAccountRemainMoney);
        log.debug("이체 받은 사람 남은 돈: {}", depositAccountCurrentMoney);

        KRWTransactionAccountDTO transactionDTO = KRWTransactionAccountDTO.builder()
                .계좌번호(DEPOSIT_SUB_ACCOUNT_NUMBER)
                .상대계좌번호(WITHDRAWAL_SUB_ACCOUNT_NUMBER)
                .입금은행코드(transferDTO.get입금은행코드())
                .이체종류("입금")
                .이체금액(TRANSFER_MONEY)
                .거래후잔액(depositAccountCurrentMoney)
                .입금계좌통장메모("입금 메모")
                .출금계좌통장메모("출금 메모")
                .build();

        log.debug("tx: {}", transactionDTO);
        bankMapper.saveAccountTransaction(transactionDTO);
        bankMapper.saveAccountTransaction(transactionDTO.revert(withdrawAccountRemainMoney));
    }
}
