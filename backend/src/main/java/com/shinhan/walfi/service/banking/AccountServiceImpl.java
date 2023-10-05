package com.shinhan.walfi.service.banking;

import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.domain.banking.Account;
import com.shinhan.walfi.domain.banking.CryptoWallet;
import com.shinhan.walfi.domain.enums.CoinType;
import com.shinhan.walfi.dto.banking.AccountDto;
import com.shinhan.walfi.dto.banking.AccountResDto;
import com.shinhan.walfi.exception.AccountErrorCode;
import com.shinhan.walfi.exception.AccountException;
import com.shinhan.walfi.exception.UserErrorCode;
import com.shinhan.walfi.exception.UserException;
import com.shinhan.walfi.repository.UserRepository;
import com.shinhan.walfi.repository.banking.CryptoWalletRepository;
import com.shinhan.walfi.util.CryptoUtil;
import com.shinhan.walfi.util.CryptoWalfiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService{

    private final UserRepository userRepository;

    private final CryptoWalletRepository cryptoWalletRepository;

    private final CryptoUtil cryptoUtil;

    private final CryptoWalfiUtil cryptoWalfiUtil;

    /**
     * 유저와 연관된 계좌들을 조회 하는 기능
     *
     * @exception 'NO_MATCHING_USER' - 해당 유저를 조회할 수 없을때 예외 발생
     * @exception 'AccountResDto' - 전송한 대표계좌가 유저의 계좌가 아니면 예외 발생
     * @param userId
     * @param userMainAccount
     * @return AccountResDto
     */
    public AccountResDto getAccounts(String userId, String userMainAccount) {

        // ========= 일반 계좌들 조회 ========
        User user = userRepository.find(userId);

        if (user == null) {
            throw new UserException(UserErrorCode.NO_MATCHING_USER);
        }

        if (!user.get대표계좌().equals(userMainAccount)) {
            log.error("=== " + userMainAccount + "은 " + userId + "의 계좌가 아닌 예외 " +" ===");
            throw new AccountException(AccountErrorCode.NOT_A_USER_ACCOUNT);
        }

        List<Account> accounts = user.getAccounts();
        List<AccountDto> accountDtoList = accounts.stream()
                .map(account -> AccountDto.accountToAccountDto(account))
                .collect(Collectors.toList());


        // ========= 가상화폐 계좌 SEP 조회 ========
        CryptoWallet sepWallet = cryptoWalletRepository.findWallet(user.get대표계좌(), CoinType.SEP);

        AccountDto sepAccountDto = AccountDto.cryptoWalletToAccountDto(sepWallet,
                cryptoUtil.checkBalance(sepWallet.getAddress()),
                cryptoUtil.convertEthToKrw(cryptoUtil.checkBalance(sepWallet.getAddress())));

        accountDtoList.add(sepAccountDto);

        // ========= 가상화폐 계좌 WALFI 조회 ========
        CryptoWallet walfiWallet = cryptoWalletRepository.findWallet(user.get대표계좌(), CoinType.WALFI);

        AccountDto walfiAccountDto = AccountDto.cryptoWalletToAccountDto(walfiWallet,
                cryptoWalfiUtil.getBalance(walfiWallet.getAddress()).toString(), "0");

        accountDtoList.add(walfiAccountDto);



        AccountResDto accountResDto = AccountResDto.getAccountResDto(userId, accountDtoList);


        return accountResDto;

    }




}
