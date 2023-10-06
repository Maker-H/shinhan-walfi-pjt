package com.shinhan.walfi.service.game;

import com.shinhan.walfi.dao.ItemDao;
import com.shinhan.walfi.domain.enums.ItemName;
import com.shinhan.walfi.domain.game.GameCharacter;
import com.shinhan.walfi.domain.game.GameItem;
import com.shinhan.walfi.domain.game.UserGameInfo;
import com.shinhan.walfi.dto.game.CharacterDto;
import com.shinhan.walfi.dto.game.CharacterListResDto;
import com.shinhan.walfi.dto.game.ItemReqDto;
import com.shinhan.walfi.dto.game.ItemResDto;
import com.shinhan.walfi.mapper.DecoMapper;
import com.shinhan.walfi.repository.game.CharacterRepository;
import com.shinhan.walfi.repository.game.UserGameInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DecoServiceImpl implements DecoService{

    private final DecoMapper decoMapper;

    @Override
    public List<ItemResDto> getList(String userId) {
        List<ItemResDto> items = decoMapper.getItemCharacter(userId);
        return items;
    }

    @Override
    public void update(ItemReqDto itemReqDto) {
        decoMapper.update(itemReqDto);
    }

    @Override
    public ItemDao create(String userId) {
        ItemDao itemDao = new ItemDao();
        itemDao.setItemName(ItemName.randomItemName());
        itemDao.setUserId(userId);
        int countSameItem = decoMapper.count(itemDao);
        if(countSameItem == 0){
            decoMapper.create(itemDao);
        }
        return itemDao;
    }

    @Override
    public List<GameItem> getItemList(String userId) {
        List<GameItem> items = decoMapper.getItemList(userId);
        return items;
    }
}
