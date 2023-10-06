package com.shinhan.walfi.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.http.HttpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@Configuration
public class EthereumConnectionConfig {

    @Value("${ethereum.service.url}")
    private String url;

    @Bean
    Web3j getEthereumConnection() throws IOException{

        String serviceUrl = url;
//        log.info("=== Ethereum sync serverIp : " + serviceUrl + "===");

        Web3j web3 = Web3j.build(new HttpService(serviceUrl));
        log.info("=== connected to ethereum server 123 === " + web3);

        Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        log.info("=== Ethereum client version ===" + clientVersion);
        return web3;
    }
}