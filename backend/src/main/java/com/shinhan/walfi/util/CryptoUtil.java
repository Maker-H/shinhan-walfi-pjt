package com.shinhan.walfi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.walfi.domain.banking.CryptoWallet;
import com.shinhan.walfi.domain.enums.CoinType;
import com.shinhan.walfi.exception.TransferException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Convert;

import javax.xml.bind.DatatypeConverter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static com.shinhan.walfi.exception.TransferErrorCode.OVERDRAWN;

@Slf4j
@Component
@RequiredArgsConstructor
public class CryptoUtil {

    @Value("${ethereum.api.key}")
    private String gasApiKey;

    private final Web3j web3j;

    private final CryptoSignUtil cryptoSignUtil;


    public String checkBalance(String address) {

        String balanceInEther = null;

        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            BigInteger balanceInWei = ethGetBalance.getBalance();
            balanceInEther = convertWeiToEth(balanceInWei);

        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("=== 잔액 {} ===", balanceInEther);
        return balanceInEther;
    }


    /**
     * eth 단위의 가스비 확인
     * @return
     */
    public String getGasFeeInEth() {
        String api = gasApiKey;

        WebClient webClient = WebClient.create();
        String url = String.format("https://api.etherscan.io/api?module=gastracker&action=gasoracle&apikey=%s", gasApiKey);

        Map<String, Object> response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String safeGasPriceInEth = null;
        if (response != null) {
            Map<String, Object> result = (Map<String, Object>) response.get("result");
            if (result != null) {
                String safeGasPriceInGWei = (String) result.get("FastGasPrice");
//                log.info("=== gwei 가스비: {} ===", safeGasPriceInGWei);
                BigInteger safeGasPriceInWei = convertGWeiToWeiWithPrice(safeGasPriceInGWei);
//                log.info("=== wei 가스비: {} ===", safeGasPriceInWei);
                safeGasPriceInEth = convertWeiToEth(new BigInteger(safeGasPriceInWei.toString()));
//                log.info("=== eth 가스비: {} ===", safeGasPriceInEth);
            }
        }

        return safeGasPriceInEth;
    }

    /**
     * 이더 1개당 원화 가격 반환
     * @return
     */
    public String convertEthToKrw(String eth) {
        String baseCurrency = "KRW";
        String targetCurrency = "ETH";

        WebClient webClient = WebClient.create();
        String url = String.format("https://api.upbit.com/v1/ticker?markets=%s-%s", baseCurrency, targetCurrency);

        List<Map> response = webClient.get()
                .uri(url)
                .header("accept", "application/json")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        Double tradePrice = null;
        if (response != null && !response.isEmpty()) {
            Map<String, Object> tickerData = response.get(0);
            tradePrice = Double.parseDouble(tickerData.get("trade_price").toString());
        }

        if (tradePrice == null) {
            // TODO: trade_price 값을 가져오지 못했을 때의 처리
        }

        long convertedPrice = (long) Math.ceil(Double.parseDouble(eth) * tradePrice);

        return String.valueOf(convertedPrice);
    }

    public BigInteger convertGWeiToWeiWithPrice(String price) {
        return  Convert.toWei(new BigDecimal(price).multiply(BigDecimal.valueOf(21000)), Convert.Unit.GWEI).toBigInteger();
    }


    public String convertWeiToEth(BigInteger wei) {
        return Convert.fromWei(new BigDecimal(wei), Convert.Unit.ETHER).toPlainString();
    }

    public BigInteger convertEthToWei(String eth) {
        BigDecimal ethInBD = new BigDecimal(eth);
        return ethInBD.multiply(BigDecimal.valueOf(10).pow(18)).toBigInteger();
    }


    /**
     * 1. json_wallet에서 파일 읽고 encpwd와 password_key로 암호 복호화 <br>
     * 2. 복호화한 암호와 json_wallet을 이용해 타원 키 쌍 복호화 <br>
     * 3. 복호화 한 타원 키 쌍으로 Credentials 재생성
     * @param cryptoWallet
     * @return
     */
    public Credentials getCredential(CryptoWallet cryptoWallet) {
        // DB에서 필요한 정보 가져오기
        String jsonWalletString = cryptoWallet.getJsonWallet();
        String encPwd = cryptoWallet.getEncpwd();  // DB에서 가져온 값
        String passwordKey =cryptoWallet.getPasswordKey();  // DB에서 가져온 값

        // JSON 문자열을 WalletFile 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        WalletFile walletFile = null;
        Credentials credentials = null;
        try {
            walletFile = objectMapper.readValue(jsonWalletString, WalletFile.class);

            // 암호화된 비밀번호 복호화
            String password = cryptoSignUtil.decrypt(encPwd, passwordKey);

            // Credentials 객체 생성
            ECKeyPair ecKeyPair = Wallet.decrypt(password, walletFile);
            credentials = Credentials.create(ecKeyPair);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (CipherException e) {
            throw new RuntimeException(e);
        }

        return credentials;
    }

    /**
     * 1. 이더 전송에는 평균 21000 가스가 소비됨
     * 2. '1 가스당' 금액은 시세에 따라 변동 (maxFeePerGasInWei)
     *
     * @param fromWallet
     * @param sendingAmount
     * @return
     */
    public String sendEthTransaction(CryptoWallet fromWallet, String toAccount, BigDecimal sendingAmount) {

        // 송신자 계정 주소 가져오기
        String fromAccount = fromWallet.getAddress();
        Credentials fromCredential = getCredential(fromWallet);


        BigDecimal balance = new BigDecimal(checkBalance(fromAccount));
        if (balance.compareTo(sendingAmount) < 0) {
            throw new TransferException(OVERDRAWN);
        }

        try {
            fromAccount = Keys.toChecksumAddress(fromAccount);
            toAccount = Keys.toChecksumAddress(toAccount);

            // TODO: 없으면 에러

            BigInteger nonce =
                    web3j.ethGetTransactionCount(fromAccount, DefaultBlockParameterName.LATEST).send().getTransactionCount();

            BigInteger valueInWei =
                    Convert.toWei(sendingAmount, Convert.Unit.ETHER).toBigInteger();

            BigInteger maxFeePerGasInWei =
                    Convert.toWei(new BigDecimal(getGasFeeInEth()), Convert.Unit.GWEI).toBigInteger();

            RawTransaction rawTransaction =
                    RawTransaction.createEtherTransaction(nonce, BigInteger.valueOf(21000), maxFeePerGasInWei, toAccount, valueInWei);

            int chainId = CoinType.SEP.getChainId();
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, fromCredential);

            String hexValue= "0x" + DatatypeConverter.printHexBinary(signedMessage);

            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

            log.info("=== Transaction hash: {} ===", ethSendTransaction.getTransactionHash());

            return ethSendTransaction.getTransactionHash();
        } catch(Exception e){
            e.printStackTrace();
        }

        return "FAIL";
    }

}
