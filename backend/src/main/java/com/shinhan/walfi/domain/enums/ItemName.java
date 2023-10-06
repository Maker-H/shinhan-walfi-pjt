package com.shinhan.walfi.domain.enums;

import lombok.Getter;

import java.util.Random;

@Getter
public enum ItemName {
    SSAFY_CAP, CROWN_CAP, RUBY_NECKLACE;

    private static final Random PRNG = new Random();

    public static ItemName randomItemName()  {
        ItemName[] Items = values();
        return Items[PRNG.nextInt(Items.length)];
    }
}
