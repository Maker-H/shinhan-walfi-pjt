package com.shinhan.walfi.dto.game;

import com.shinhan.walfi.domain.enums.CharacterType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemResDto {

    private Long itemIdx;

    private String itemName;

    private int y;

    private int x;

    private double rotation;

    private double size;

    private String characterIdx;

    private CharacterType characterType;
}
