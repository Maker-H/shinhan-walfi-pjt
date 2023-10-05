package com.shinhan.walfi.controller;

import com.shinhan.walfi.dao.DailyQuestDao;
import com.shinhan.walfi.mapper.QuestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/quest")
@RequiredArgsConstructor
public class QuestController {

    private final QuestMapper questMapper;

    @GetMapping()
    public List<DailyQuestDao> getUserDailyQuest(@RequestParam String userId) {

        List<DailyQuestDao> userDailyQuest = questMapper.getUserDailyQuest(userId);
        return userDailyQuest;
    }
}
