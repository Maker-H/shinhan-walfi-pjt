package com.shinhan.walfi.service.game;

import com.shinhan.walfi.dto.game.BattleRankResDto;
import com.shinhan.walfi.dto.game.BattleReqDto;
import com.shinhan.walfi.dto.product.ProductResDto;

import java.util.List;

public interface BattleService {

    void write(BattleReqDto battleReqDto);

    List<BattleRankResDto> getRank(Long idx);

    List<BattleRankResDto> getAllRank();

    ProductResDto getRate(String userId);

    void getBattleCount(String userId);

    ProductResDto getUserBattleHistoryCount(String userId);
}
