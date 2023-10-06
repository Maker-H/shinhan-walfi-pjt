package com.shinhan.walfi.domain.game;

import com.shinhan.walfi.domain.enums.ItemName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class GameItem {

    @Id
    private Long itemIdx;

    @Enumerated(EnumType.STRING)
    private ItemName itemName;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private UserGameInfo userGameInfo;
}
