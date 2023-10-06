package com.shinhan.walfi.util;

import com.shinhan.walfi.domain.enums.LevelUp;
import com.shinhan.walfi.domain.game.GameCharacter;
import com.shinhan.walfi.domain.game.UserGameInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CharacterStatusUtil {

    public GameCharacter updateExp(int statusValue, GameCharacter character) {
        LevelUp defaultLevel = character.getLevel();
        int defaultExp = character.getExp();
        int totalRaiseExp = defaultExp + statusValue;
        int newExp = 0;

        // exp가 사용자의 현재 레벨을 초과하여 오르는 경우
        if (defaultLevel.getNeededExp() <= totalRaiseExp) {

            LevelUp newLevel = null;

            // 사용자의 경험치가 이미 max인 경우 레벨과 경험치가 더 오를 수 없음
            if (defaultLevel.equals(LevelUp.LEVEL_10)) {
                character.setLevel(LevelUp.LEVEL_10);
                character.setExp(LevelUp.LEVEL_10.getNeededExp());
            } else {
                int sumExp = 0;
                for (LevelUp l : LevelUp.values()) {
                    if (l.getLevel() < defaultLevel.getLevel()) {
                        continue;
                    }

                    sumExp += l.getNeededExp();

                    if (totalRaiseExp < sumExp) {
                        newLevel = LevelUp.getLevelUpByLevel(l.getLevel());
                        newExp = l.getNeededExp() - (sumExp - totalRaiseExp);

                        break;
                    }
                }
                character.setLevel(newLevel);
                character.setExp(newExp);
            }

        } else {
            newExp = defaultExp + statusValue;
            character.setExp(newExp);
        }

        return character;
    }

    public UserGameInfo updatePoint(int updatePont, UserGameInfo userGameInfo) {
        int defaultPoint = userGameInfo.getPoint();
        userGameInfo.setPoint(defaultPoint + updatePont);
        return userGameInfo;
    }
}
