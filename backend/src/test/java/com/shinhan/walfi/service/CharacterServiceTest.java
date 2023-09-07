package com.shinhan.walfi.service;

import com.shinhan.walfi.domain.TierPerColor;
import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.domain.game.GameCharacter;
import com.shinhan.walfi.domain.game.UserGameInfo;
import com.shinhan.walfi.dto.game.CharacterResDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
class CharacterServiceTest {
    @PersistenceContext
    EntityManager em;

    @Autowired CharacterService characterService;

    @Test
    @DisplayName("캐릭터 생성 확인 테스트 (색, 레벨, hp, exp, atk, def, ismain)")
    public void createCharacter() {
        // given
        User user = new User();
        user.setUserId("ssafy");
        em.persist(user);

        // when
        Long findGameIdx = characterService.create(user.getUserId());
        GameCharacter findGameCharacter = em.find(GameCharacter.class, findGameIdx);

        // then
        Assertions.assertThat(findGameCharacter.getColor()).isEqualTo(TierPerColor.BASIC);
        Assertions.assertThat(findGameCharacter.getLevel()).isEqualTo(1);
        Assertions.assertThat(findGameCharacter.getHp()).isEqualTo(100);
        Assertions.assertThat(findGameCharacter.getExp()).isEqualTo(0);
        Assertions.assertThat(findGameCharacter.getAtk()).isEqualTo(0);
        Assertions.assertThat(findGameCharacter.getDef()).isEqualTo(0);
        Assertions.assertThat(findGameCharacter.isMain()).isEqualTo(true);
    }

    @Test
    @DisplayName("캐릭터 뽑기 확인 테스트 (색, 레벨, hp, exp, atk, def, ismain)")
    public void shopCharacter() {
        // given
        User user = new User();
        user.setUserId("ssafy");
        em.persist(user);

        // when
        Long findGameIdx = characterService.shop(user.getUserId());
        GameCharacter findGameCharacter = em.find(GameCharacter.class, findGameIdx);

        // then
        Assertions.assertThat(findGameCharacter.getColor()).isEqualTo(TierPerColor.BASIC);
        Assertions.assertThat(findGameCharacter.getLevel()).isEqualTo(1);
        Assertions.assertThat(findGameCharacter.getHp()).isEqualTo(100);
        Assertions.assertThat(findGameCharacter.getExp()).isEqualTo(0);
        Assertions.assertThat(findGameCharacter.getAtk()).isEqualTo(0);
        Assertions.assertThat(findGameCharacter.getDef()).isEqualTo(0);
        Assertions.assertThat(findGameCharacter.isMain()).isEqualTo(false);
    }

    @Test
    @DisplayName("userGameId를 이용한 캐릭터 조회")
    void searchCharacter() throws Exception{
        // given
        UserGameInfo userGameInfo = new UserGameInfo();
        userGameInfo.setUserGameId("1234");
        em.persist(userGameInfo);

        Long c1 = characterService.create(userGameInfo.getUserGameId());
        Long c2 = characterService.shop(userGameInfo.getUserGameId());
        Long c3 = characterService.shop(userGameInfo.getUserGameId());

        // when
        CharacterResDto characterResDto = characterService.searchCharacters(userGameInfo.getUserGameId());

        // then
        Assertions.assertThat(characterResDto.getCharacterDtoList().size()).isEqualTo(3);
    }
}