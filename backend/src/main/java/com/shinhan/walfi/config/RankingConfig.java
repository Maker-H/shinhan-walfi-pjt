package com.shinhan.walfi.config;

import com.shinhan.walfi.mapper.BattleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RankingConfig {

    private final BattleMapper battleMapper;

    @Scheduled(cron = "0 0 9 1 * *", zone = "Asia/Seoul")
    public void updateRanking() {
        log.info("initialize Ranking Time : {}", new Timestamp(System.currentTimeMillis()));
        battleMapper.initializeRanking();
    }

    @Scheduled(cron = "0 0 9 15 * *", zone = "Asia/Seoul")
    public void initRanking() {
        battleMapper.deleteRanking();
    }
}
