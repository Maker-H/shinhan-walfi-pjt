package com.shinhan.walfi.service.banking;

import com.shinhan.walfi.dto.banking.ExchangeResDto;
import org.json.simple.parser.ParseException;

public interface ExchangeService {

    ExchangeResDto getLatest() throws ParseException;

    void toGlobalExchange(String userId, String 사용자대표계좌, String 도착계좌통화코드, long 금액, float 전신환매도환율);

    void fromGlobalExchange(String userId, String 사용자대표계좌, String 출발계좌통화코드, long 금액, float 전신환매입환율);


}
