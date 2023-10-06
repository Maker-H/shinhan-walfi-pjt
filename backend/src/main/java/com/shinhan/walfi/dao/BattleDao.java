package com.shinhan.walfi.dao;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BattleDao {

    private String userId;

    private Long branchIdx;

    private LocalDateTime startTime;

}
