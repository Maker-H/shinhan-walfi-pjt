package com.shinhan.walfi.dto.game;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class GoodsCharacterResDto {

    private Long goodsIdx;

    private String goodsType;

    private BigDecimal price;

    private LocalDateTime createTime;

    private Long characterIdx;

    private String characterType;

    private String color;

    private String seller;
}
