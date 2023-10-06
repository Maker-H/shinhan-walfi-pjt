package com.shinhan.walfi.repository.game;

import com.shinhan.walfi.domain.enums.LevelUp;
import com.shinhan.walfi.domain.game.GameCharacter;
import com.shinhan.walfi.domain.game.UserGameInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.ILoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CharacterRepository {

    private final EntityManager em;

    public GameCharacter save(GameCharacter gameCharacter) {
        em.persist(gameCharacter);
        return gameCharacter;
    }

    public GameCharacter findCharacterByIdx(Long characterIdx){
        GameCharacter gameCharacter = em.find(GameCharacter.class, characterIdx);
        return gameCharacter;
    }

    public GameCharacter findMainCharacter(UserGameInfo userGameInfo) {
        GameCharacter result = em.createQuery(
                        "select g from GameCharacter g " +
                                "where g.userGameInfo=:userGameInfo " +
                                "and g.isMain=true", GameCharacter.class)
                .setParameter("userGameInfo", userGameInfo)
                .getSingleResult();

        return result;
    }

    public int findMaxLevelCharacterNum(UserGameInfo userGameInfo) {
        LevelUp maxLevel = LevelUp.LEVEL_10;
        Long maxLevelCharacterNum = em.createQuery("select count(*) from GameCharacter g " +
                        "where g.userGameInfo=:userGameInfo " +
                        "and g.level=:maxLevel", Long.class)
                .setParameter("userGameInfo", userGameInfo)
                .setParameter("maxLevel", maxLevel)
                .getSingleResult();

        return maxLevelCharacterNum.intValue();
    }

}
