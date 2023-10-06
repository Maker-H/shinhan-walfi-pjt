package com.shinhan.walfi.domain.game;

import com.shinhan.walfi.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class UserGameInfo {

    @Id
    private String userId;

    private int point;

    private String status;

    private int battleCount;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "userGameInfo", fetch = LAZY)
    private List<Branch> branches = new ArrayList<>();

    @OneToMany(mappedBy = "userGameInfo", fetch = LAZY)
    private List<GameCharacter> gameCharacters = new ArrayList<>();


}
