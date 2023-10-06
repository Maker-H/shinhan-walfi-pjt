package com.shinhan.walfi.domain.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum LevelUp {

    LEVEL_01(1, 40),
    LEVEL_02(2, 80),
    LEVEL_03(3, 160),
    LEVEL_04(4, 250),
    LEVEL_05(5, 500),
    LEVEL_06(6, 700),
    LEVEL_07(7, 1400),
    LEVEL_08(8, 3200),
    LEVEL_09(9, 4000),
    LEVEL_10(10,5000);

    final int level;
    final int neededExp;

    LevelUp(int level, int neededExp) {
        this.neededExp = neededExp;
        this.level = level;
    }

    // 레벨 값을 키로 사용하여 LevelUp 열거형을 매핑하는 Map 생성
    private static final Map<Integer, LevelUp> levelMap = new HashMap<>();

    static {
        for (LevelUp levelUp : values()) {
            levelMap.put(levelUp.level, levelUp);
        }
    }

    // 레벨 값을 기반으로 LevelUp 열거형을 가져오는 정적 메서드
    public static LevelUp getLevelUpByLevel(int level) {
        return levelMap.get(level);
    }
}
