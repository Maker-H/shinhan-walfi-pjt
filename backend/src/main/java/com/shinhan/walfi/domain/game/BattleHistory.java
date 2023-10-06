package com.shinhan.walfi.domain.game;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class BattleHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long battleIdx;

    private String userId;

    private Long occupyTime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "branch_idx")
    private Branch branch;

    public void setBranch(Branch branch) {
        this.branch = branch;
        branch.getBattleHistories().add(this);
    }
}
