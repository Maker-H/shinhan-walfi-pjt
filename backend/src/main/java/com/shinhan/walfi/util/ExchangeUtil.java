package com.shinhan.walfi.util;

import com.shinhan.walfi.domain.banking.ExchangeHistory;
import com.shinhan.walfi.dto.banking.ExchangeDto;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ExchangeUtil {

    private final String baseUrl = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=";

    private final String apikey = "QVWvuzOjXchQ14bAZwWTdCDrzkYIAmBP";

    private final RestTemplate restTemplate = new RestTemplate();

    public List<ExchangeHistory> getTodayExchange() throws ParseException { //오늘의 환율정보 리스트 반환
        StringBuilder url = new StringBuilder(baseUrl);
        url.append(apikey);
        url.append("&searchdate=");
        Date now = new Date(); // 오늘 날짜 6자리
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String today = format.format(now);
        url.append(today);
        url.append("&data=AP01");

        HttpEntity<String> response = restTemplate.getForEntity(url.toString(), String.class);

        String responseBody = response.getBody(); // response를 파싱하여 환율 정보만 추출
        log.debug(responseBody);
        JSONParser parser = new JSONParser();
        JSONArray jsonExchangeList = (JSONArray) parser.parse(responseBody);

        List<ExchangeHistory> list = new ArrayList<>(); // json 객체를 Exchange list 객체로 파싱
        int[] countryIdx = {1,6,8,12,22}; // 5개국에 대해서만 (호주 중국 유럽 일본 미국 ) // Todo : 순서 상관?
        for (int c = 0; c < countryIdx.length; c++) {
            JSONObject item = (JSONObject) jsonExchangeList.get(countryIdx[c]);
            list.add(this.makeExchangEntity(now, item)); // json 객체를 ExchangeDto로 파싱
            if(c == 3){
                list.get(3).set통화코드(list.get(3).get통화코드().substring(0,3));
            }
        }
        return list;
    }

    private ExchangeHistory makeExchangEntity(Date now, JSONObject item) { // json 객체를 ExchangeDto로 파싱
        ExchangeHistory dto = ExchangeHistory.builder().
                고시일자(now).
                통화명((String) item.get("cur_nm")).
                매매기준환율(Float.parseFloat(((String) item.get("deal_bas_r")).replaceAll(",",""))).
                통화코드((String) item.get("cur_unit")).
                전신환매도환율(Float.parseFloat(((String) item.get("ttb")).replaceAll(",",""))).
                전신환매입환율(Float.parseFloat(((String) item.get("tts")).replaceAll(",",""))).
                build();
        return dto;
    }

    public ExchangeHistory getCertainExchangeRate(final String CURRENCY_CODE) throws ParseException {
        List<ExchangeHistory> todayExchange = getTodayExchange();

        ExchangeHistory findExchangeDTO = todayExchange.stream()
                .filter(ExchangeDto -> CURRENCY_CODE.equals(ExchangeDto.get통화코드()))
                .findFirst().orElse(null);
//                .orElseThrow(AccountNotFoundException::new);

        return findExchangeDTO;
    }
}
