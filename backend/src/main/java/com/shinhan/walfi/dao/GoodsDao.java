package com.shinhan.walfi.dao;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class GoodsDao {

    private Long goodsIdx;

    private String goodsType;

    private BigDecimal price;

    private LocalDateTime createTime;

    private Long characterIdx;

    private Long itemIdx;
}
