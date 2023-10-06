package com.shinhan.walfi.config;

import com.shinhan.walfi.domain.banking.ExchangeHistory;
import com.shinhan.walfi.repository.banking.ExchangeRepository;
import com.shinhan.walfi.util.ExchangeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ExchangeConfig {

    private final ExchangeUtil exchangeUtil;

    private final ExchangeRepository exchangeRepository;

    @Scheduled(cron = "0 10 10 * * *", zone = "Asia/Seoul") // 환율 고시 시간 매일 10시
    public void updateRanking() throws ParseException {
        List<ExchangeHistory> list = exchangeUtil.getTodayExchange();

        List<ExchangeHistory> yesterDayList = exchangeRepository.findYesterday();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).set전일대비(
                    Math.round((yesterDayList.get(i).get매매기준환율() - list.get(i).get매매기준환율()) * 100) / 100F
            ); // 전일 대비 값이 생각보다 크게 나온다 예상은 -1~1정도였는데 10 이렇게 나옴
        } // Todo : 전일대비 환율 카드에 대한 밸런스 조절
        exchangeRepository.saveAll(list);
        log.info("오늘의 환율 정보를 받아옵니다.");
    }
}
