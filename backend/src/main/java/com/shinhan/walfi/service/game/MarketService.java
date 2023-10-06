package com.shinhan.walfi.service.game;

import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.dto.game.BuyReqDto;
import com.shinhan.walfi.dto.game.GoodsCharacterResDto;
import com.shinhan.walfi.dto.game.GoodsItemResDto;
import com.shinhan.walfi.dto.game.MarketReqDto;

import java.util.List;

public interface MarketService {

    List<GoodsItemResDto> getItemList();

    List<GoodsCharacterResDto> getCharacterList();

    void sell(MarketReqDto marketReqDto);

    void buy(User userId, BuyReqDto buyReqDto);

}
