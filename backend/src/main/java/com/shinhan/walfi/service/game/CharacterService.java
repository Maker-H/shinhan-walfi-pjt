package com.shinhan.walfi.service.game;

import com.shinhan.walfi.dto.game.CharacterListResDto;
import com.shinhan.walfi.dto.game.CharacterWithUserIdResDto;
import com.shinhan.walfi.dto.product.ProductResDto;

public interface CharacterService {

    CharacterWithUserIdResDto create(String userId);

    CharacterWithUserIdResDto shop(String userId);

    CharacterListResDto shopTen(String userId);

    CharacterListResDto searchCharacters(String userId);

    CharacterWithUserIdResDto searchMainCharacter(String userId);

    CharacterWithUserIdResDto changeCharacterColor(String userId, Long mainCharacterIdx);

    CharacterWithUserIdResDto updateCharacterStatus(String userId,
                                                    Long characterIdx,
                                                    String statusType,
                                                    int statusValue,
                                                    String act);

    CharacterWithUserIdResDto updateCharacterStatus(String userId,
                                                    Long characterIdx,
                                                    String statusType,
                                                    int statusValue);

    ProductResDto getMaxLevelCharacterNum(String userId);
}
