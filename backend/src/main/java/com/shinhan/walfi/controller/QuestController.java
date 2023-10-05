package com.shinhan.walfi.controller;

import com.shinhan.walfi.dao.DailyQuestDao;
import com.shinhan.walfi.domain.game.GameCharacter;
import com.shinhan.walfi.domain.game.UserGameInfo;
import com.shinhan.walfi.dto.QuestRewordDto;
import com.shinhan.walfi.mapper.QuestMapper;
import com.shinhan.walfi.repository.game.CharacterRepository;
import com.shinhan.walfi.repository.game.UserGameInfoRepository;
import com.shinhan.walfi.util.CharacterStatusUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/quest")
@RequiredArgsConstructor
public class QuestController {

    private final QuestMapper questMapper;
    private final CharacterStatusUtil characterStatusUtil;

    private final CharacterRepository characterRepository;
    private final UserGameInfoRepository userGameInfoRepository;

    @GetMapping()
    public List<DailyQuestDao> getUserDailyQuest(@RequestParam String userId) {

        List<DailyQuestDao> userDailyQuest = questMapper.getUserDailyQuest(userId);
        return userDailyQuest;
    }

    @PostMapping
    @Transactional
    public void offerQuestReward(@RequestBody QuestRewordDto questRewordDto) {

        String userId = questRewordDto.getUserId();
        long questIdx = questRewordDto.getQuestIdx();

        boolean isCompleted = questMapper.checkQuestIsCompleted(userId, questIdx);
        if (!isCompleted) {
            log.error("퀘스트가 완료되지 않음");
            return;
        }

        UserGameInfo user = userGameInfoRepository.findById(userId);
        GameCharacter winner = characterRepository.findMainCharacter(user);
        characterStatusUtil.updateExp(50, winner);
        characterStatusUtil.updatePoint(50, user);

        questMapper.updateQuestStatus(userId, questIdx, 2);
    }
}
