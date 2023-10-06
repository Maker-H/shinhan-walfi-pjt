package com.shinhan.walfi.domain.game;

import com.shinhan.walfi.domain.User;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class Topten {
    @Id
    int ranking;

    @Column()
    private BigDecimal rate;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
