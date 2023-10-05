package com.shinhan.walfi.dao;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class DailyQuestDao {

    private String type;

    private String title;

    private int count;

    private int total;

    private int status;
}
