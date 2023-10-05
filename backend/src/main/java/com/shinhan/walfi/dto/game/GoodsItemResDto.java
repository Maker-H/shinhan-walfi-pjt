package com.shinhan.walfi.dto.game;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class GoodsItemResDto {

    private Long goodsIdx;

    private String goodsType;

    private BigDecimal price;

    private LocalDateTime createTime;

    private Long itemIdx;

    private String seller;

    private String itemName;
}
