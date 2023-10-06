package com.shinhan.walfi.dto.game;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CharacterWithUserIdResDto {

    String userId;

    CharacterDto characterDto;

}
