package com.shinhan.walfi.util;

import com.shinhan.walfi.config.WALFI;
import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.domain.banking.CryptoWallet;
import com.shinhan.walfi.domain.enums.CoinType;
import com.shinhan.walfi.repository.banking.CryptoWalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;

import java.math.BigInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class CryptoWalfiUtil {

    private final Web3j web3j;

    private final CryptoUtil cryptoUtil;

    private final CryptoWalletRepository cryptoWalletRepository;

    public BigInteger getBalance(String targetAddress) {
        WALFI walfi = new WALFI("0xE09BEe9686Db056AdE72640D1Da499CB147EF71A",
                web3j,
                cryptoUtil.getCredential(cryptoWalletRepository.findWallet("110001785538", CoinType.SEP)),
                cryptoUtil.convertEthToWei(cryptoUtil.getGasFeeInEth()),
                BigInteger.valueOf(1L));

        RemoteFunctionCall<BigInteger> bigIntegerRemoteFunctionCall = walfi.balanceOf(targetAddress);


        try {
            return bigIntegerRemoteFunctionCall.send();
        } catch (Exception e) {
            log.info(" === Error while fetching balance: " + e.getMessage());
        }

        return null;
    }

//    cryptoUtil.convertEthToWei(cryptoUtil.getGasFeeInEth())
}
