package com.shinhan.walfi.domain.enums;

import lombok.Getter;

@Getter
public enum CoinType {
    ETH(1),
    SEP(11155111),
    WALFI(11155111);

    final int chainId;

    CoinType(int chainId) {
        this.chainId = chainId;
    }
}
