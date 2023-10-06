package com.shinhan.walfi.domain.game;

import com.shinhan.walfi.domain.enums.CharacterType;
import com.shinhan.walfi.domain.enums.LevelUp;
import com.shinhan.walfi.domain.enums.TierPerColor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long characterIdx;

    @Enumerated(EnumType.STRING)
    private CharacterType characterType;

    @Enumerated(EnumType.STRING)
    private TierPerColor color;

    @Enumerated(EnumType.STRING)
    private LevelUp level;

    private int exp;

    private int hp;

    private int atk;

    private int def;

    private boolean isMain;

    private int y;

    private int x;

    private int rotation;

    private double size;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private UserGameInfo userGameInfo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_idx")
    private GameItem gameItem;

    /**
     * 게임 캐릭터 생성, enum은 service에서 설정
     * @param userGameInfo
     */
    public static GameCharacter createCharacter(UserGameInfo userGameInfo, CharacterType characterType, Boolean isMain) {
        GameCharacter gameCharacter = new GameCharacter();

        gameCharacter.characterType = characterType;
        gameCharacter.color = TierPerColor.BASIC;
        gameCharacter.level = LevelUp.LEVEL_01;
        gameCharacter.exp = 0;
        gameCharacter.hp = 50;
        gameCharacter.atk = 0;
        gameCharacter.def = 0;
        gameCharacter.isMain = isMain;
        gameCharacter.userGameInfo = userGameInfo;

        gameCharacter.addCharacterToUserGameInfo(userGameInfo);

        return gameCharacter;
    }

    private void addCharacterToUserGameInfo(UserGameInfo userGameInfo) {
        userGameInfo.getGameCharacters().add(this);
    }
}
