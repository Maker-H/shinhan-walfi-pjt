package com.shinhan.walfi.service.banking;

import com.shinhan.walfi.dto.banking.GasResDto;
import com.shinhan.walfi.repository.banking.CryptoWalletRepository;
import com.shinhan.walfi.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CryptoServiceImpl implements CryptoService{

    private final CryptoWalletRepository cryptoWalletRepository;

    private final CryptoUtil cryptoUtil;

    @Override
    public GasResDto getGasFee() {

        String ethGasFee = cryptoUtil.getGasFeeInEth();
        String krwGasPrice = cryptoUtil.convertEthToKrw(ethGasFee);

        log.info("=== krwGasPrice: {} ===", krwGasPrice);
        log.info("=== safeGasPriceInEth: {} ==== ", ethGasFee);
        return GasResDto.getGasResDto(ethGasFee, krwGasPrice);
    }

}
