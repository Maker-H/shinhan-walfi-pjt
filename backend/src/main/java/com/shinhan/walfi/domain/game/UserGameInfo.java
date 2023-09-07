package com.shinhan.walfi.domain.game;

import com.shinhan.walfi.domain.User;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class UserGameInfo {

    @Id
    private String userGameId;

    private int point;

    private String status;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "userGameInfo")
    private List<Branch> branches = new ArrayList<>();

    @OneToMany(mappedBy = "userGameInfo")
    private List<GameCharacter> gameCharacters = new ArrayList<>();

}