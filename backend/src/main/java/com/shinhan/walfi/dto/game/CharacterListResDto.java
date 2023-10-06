package com.shinhan.walfi.dto.game;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
public class CharacterListResDto {

    String userId;

    List<CharacterDto> characterDtoList;

}
