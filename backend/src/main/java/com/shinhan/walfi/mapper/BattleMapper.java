package com.shinhan.walfi.mapper;

import com.shinhan.walfi.dao.BattleDao;
import com.shinhan.walfi.dto.game.BattleRankResDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BattleMapper {

    void write(BattleDao dao);

    List<BattleRankResDto> getRank(Long idx);

    List<BattleRankResDto> getAllRank();

    void initializeRanking();

    int cntTop(String userId);

    double getRate(String userId);

    void deleteRanking();
}
