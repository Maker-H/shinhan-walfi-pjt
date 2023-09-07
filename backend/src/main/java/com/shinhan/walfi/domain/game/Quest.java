package com.shinhan.walfi.domain.game;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questIdx;

    private String questType;

    private String questTitle;

    private Integer questExp;

    private String context;

}
