package com.shinhan.walfi.repository.game;


import com.shinhan.walfi.domain.game.GameCharacter;
import com.shinhan.walfi.domain.game.UserGameInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class UserGameInfoRepository {

    private final EntityManager em;

    public UserGameInfo findById(String userId) {
        return em.find(UserGameInfo.class, userId);
    }

    public UserGameInfo save(UserGameInfo userGameInfo) {
        em.persist(userGameInfo);
        return userGameInfo;
    }

}
