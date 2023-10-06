package com.shinhan.walfi.domain.enums;

import lombok.Getter;

@Getter
public enum ProbabilityPerTier { // 10, 5, 84.9, 0.1

    NORMAL(33),
    EPIC(33),
    UNIQUE(33),
    LEGENDARY(1);

    final double percent;

    ProbabilityPerTier(double percent){
        this.percent = percent;
    }
}
