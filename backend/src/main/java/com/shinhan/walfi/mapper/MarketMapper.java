package com.shinhan.walfi.mapper;

import com.shinhan.walfi.dao.GoodsDao;
import com.shinhan.walfi.domain.enums.CharacterType;
import com.shinhan.walfi.dto.game.GoodsCharacterResDto;
import com.shinhan.walfi.dto.game.GoodsItemResDto;
import com.shinhan.walfi.dto.game.MarketReqDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface MarketMapper {

    List<GoodsItemResDto> getItemList();

    List<GoodsCharacterResDto> getCharacterList();

    void sell(MarketReqDto marketReqDto);

    GoodsDao findById(Long goodsIdx);

    void delete(Long goodsIdx);

    int count(CharacterType characterType, String userId);

    String findUserByCId(Long characterIdx);

    String findUserByIId(Long itemIdx);

    void setMain(Long characterIdx);
}
