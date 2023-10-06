package com.shinhan.walfi.domain.enums;

public enum QuestExp {

    DAILY(30),
    WEEKLY(60),
    MONTHLY(100),
    SPECIAL(200);

    final int exp;

    QuestExp(int exp){
        this.exp = exp;
    }
}
