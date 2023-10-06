package com.shinhan.walfi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.domain.banking.CryptoWallet;
import com.shinhan.walfi.domain.enums.CoinType;
import com.shinhan.walfi.repository.banking.CryptoWalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.crypto.*;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CryptoCreateUtil {

    private final CryptoWalletRepository cryptoWalletRepository;

    private final CryptoSignUtil cryptoSignUtil;

    /**
     * 가상화폐 계좌 생성
     *
     * @throws IOException
     */
    public void createCryptoWallet(User user, CoinType coinType) {

        Map<String, Object> map;

        // 계좌 생성
        map = createCredentials();

        String jsonWalletString = (String) map.get("jsonWalletString");
        String encPwd = (String) map.get("encPwd");
        String passwordKey = (String) map.get("passwordKey");
        String address = (String) map.get("address");

        // 다른 코인 계좌 만들고 싶으면 여기서 변경...
        CryptoWallet cryptoWallet = CryptoWallet.createCryptoWallet(jsonWalletString, encPwd, passwordKey, address, user, coinType);
        cryptoWalletRepository.save(cryptoWallet);
        log.info("=== {} 코인에 대한 {} 계좌 생성", CoinType.SEP, address);
    }

    /**
     * 1. 임의의 비밀번호 생성 <br>
     * 2. 비밀번호와 타원 곡선 암호화 쌍으로 json_wallet 파일 생성 <br>
     * 3. 잠금 키로 비밀번호 암호화 <br>
     * 4. 암호화 한 비밀번호, 잠금키, json_wallet 저장 <br>
     *
     * @return
     */
    private Map<String, Object> createCredentials() {
        Map<String, Object> map = new HashMap<>();
        try {
            // 랜덤한 UUID를 생성하여 비밀번호로 사용
            String password = UUID.randomUUID().toString().replaceAll("-", "");

            // 새로운 ECKeyPair(타원 곡선 암호화 키 쌍)을 생성
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();

            // 생성된 키 쌍과 비밀번호를 이용하여 WalletFile 객체 생성
            WalletFile walletFile = Wallet.createStandard(password, ecKeyPair);

            // 비밀번호 안호화하는데 사용하는 키
            String passwordKey = cryptoSignUtil.getSecretKey();

            // 비밀번호 암호화 (위에서 생성한 password를 passwordKey로 암호화)
            String encPwd = cryptoSignUtil.encrypt(password, passwordKey);

            ObjectMapper objectMapper = new ObjectMapper();

            String jsonWalletString = objectMapper.writeValueAsString(walletFile);

            //ECKeyPair 객체로 Credentials 객체 생성, 거래를 생성하거나 서명할 때 사용
            String address = Credentials.create(ecKeyPair).getAddress();

            // 암호화하지 않은 비밀번호

            map.put("jsonWalletString", jsonWalletString);
            map.put("encPwd", encPwd);
            map.put("passwordKey", passwordKey);
            map.put("address", address);


        } catch (InvalidAlgorithmParameterException e) {
        } catch (CipherException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchProviderException e) {
        } catch (JsonProcessingException e) {
        }
        return map;

    }


}

