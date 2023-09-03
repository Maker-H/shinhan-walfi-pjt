package com.shinhan.walfi.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "user_game_info")
public class UserGameInfo {
    @Id
    @Column(name = "user_code")
    private String userCode;

    @NotNull
    private Integer point;

    @NotNull
//    @ColumnDefault("C") //challenger or manager
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
