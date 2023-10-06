package com.shinhan.walfi.domain.game;

import com.shinhan.walfi.domain.banking.BranchCurrency;
import com.shinhan.walfi.domain.enums.CharacterType;
import com.shinhan.walfi.domain.enums.LevelUp;
import com.shinhan.walfi.domain.enums.TierPerColor;
import com.shinhan.walfi.dto.game.BranchDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Branch {

    @Id
    private Long branchIdx;

    private String address;

    private String branchName;

    private String branchType;

    private String branchPhoneNumber;

    private double latitude;

    private double longitude;

    @Enumerated(EnumType.STRING)
    private LevelUp managerLevel;

    @Enumerated(EnumType.STRING)
    private CharacterType managerAnimalType;

    @Enumerated(EnumType.STRING)
    private TierPerColor managerAnimalColor;

    private int managerExp;

    private int managerHp;

    private int managerAtk;

    private int managerDef;

    @CreationTimestamp
    private LocalDateTime startTime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private UserGameInfo userGameInfo;

    @OneToMany(mappedBy = "branch")
    private List<BattleHistory> battleHistories = new ArrayList<>();

    @OneToMany(mappedBy = "branch")
    private List<BranchCurrency> branchCurrencies = new ArrayList<>();

    public void setManagerLevel(LevelUp managerLevel) {
        this.managerLevel = managerLevel;
    }

    public void setManagerExp(int managerExp) {
        this.managerExp = managerExp;
    }

    public void setManagerHp(int managerHp) {
        this.managerHp = managerHp;
    }

    public void setManagerAtk(int managerAtk) {
        this.managerAtk = managerAtk;
    }

    public void setManagerDef(int managerDef) {
        this.managerDef = managerDef;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setUserGameInfo(UserGameInfo userGameInfo) {
        this.userGameInfo = userGameInfo;
        userGameInfo.getBranches().add(this);
    }

    public BranchDto entityToDto() {
        BranchDto branchDto = new BranchDto(branchIdx, branchName, address, branchPhoneNumber, latitude, longitude, managerAnimalType.name(), managerAnimalColor.name(), managerLevel.getLevel(), managerExp, managerHp, managerAtk, managerDef);
        return branchDto;
    }
}
