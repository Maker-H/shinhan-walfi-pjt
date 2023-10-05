package com.shinhan.walfi.domain.game;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Goods {

    @Id
    private Long goodsIdx;

    private String goodsType;

    private BigDecimal price;

    private LocalDateTime createTime;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "characterIdx")
    private GameCharacter gameCharacter;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "itemIdx")
    private GameItem gameItem;

}
