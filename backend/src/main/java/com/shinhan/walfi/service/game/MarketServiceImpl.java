package com.shinhan.walfi.service.game;

import com.shinhan.walfi.dao.GoodsDao;
import com.shinhan.walfi.dao.ItemDao;
import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.domain.banking.CryptoWallet;
import com.shinhan.walfi.domain.enums.CoinType;
import com.shinhan.walfi.domain.enums.ItemName;
import com.shinhan.walfi.domain.game.GameCharacter;
import com.shinhan.walfi.domain.game.UserGameInfo;
import com.shinhan.walfi.dto.game.BuyReqDto;
import com.shinhan.walfi.dto.game.GoodsCharacterResDto;
import com.shinhan.walfi.dto.game.GoodsItemResDto;
import com.shinhan.walfi.dto.game.MarketReqDto;
import com.shinhan.walfi.mapper.DecoMapper;
import com.shinhan.walfi.mapper.MarketMapper;
import com.shinhan.walfi.repository.banking.CryptoWalletRepository;
import com.shinhan.walfi.repository.game.CharacterRepository;
import com.shinhan.walfi.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {

    private final MarketMapper marketMapper;

    private final CharacterRepository characterRepository;

    private final CryptoWalletRepository cryptoWalletRepository;

    private final DecoMapper decoMapper;

    private final CryptoUtil cryptoUtil;

    @Override
    public List<GoodsItemResDto> getItemList() {
        List<GoodsItemResDto> itemList = marketMapper.getItemList();
        return itemList;
    }

    @Override
    public List<GoodsCharacterResDto> getCharacterList() {
        List<GoodsCharacterResDto> characterList = marketMapper.getCharacterList();
        return characterList;
    }

    @Override
    public void sell(MarketReqDto marketReqDto) {
        marketMapper.sell(marketReqDto);
    }

    @Override
    @Transactional
    public void buy(User user, BuyReqDto buyReqDto) {
        // 대표계좌로 이더리움 계좌 조회
         CryptoWallet cryptoWallet = cryptoWalletRepository.findWallet(user.get대표계좌(), CoinType.SEP);

         // 판매자 주소 조회
        GoodsDao goods = marketMapper.findById(buyReqDto.getGoodsIdx());
        String toAddress;
        if("c".equals(goods.getGoodsType())){
            toAddress = marketMapper.findUserByCId(goods.getCharacterIdx());
        } else{
            toAddress = marketMapper.findUserByIId(goods.getItemIdx());
        }

        // 돈 송금
        cryptoUtil.sendEthTransaction(cryptoWallet, toAddress, buyReqDto.getPrice());

        // 해당 아이템 또는 캐릭터를 사용자의 명의로 이동, 그전에 사용자가 해당 캐릭터, 아이템을 이미 가지고 있는지 확인
        if ("c".equals(goods.getGoodsType())) { // Character Type
            // 캐릭터 보유 여부 확인
            GameCharacter character = characterRepository.findCharacterByIdx(goods.getCharacterIdx());
            int cnt = marketMapper.count(character.getCharacterType(), user.getUserId());

            if (cnt != 0) { // 이미 보유한 캐릭터, 구매할 수 없음 exception
                log.debug("이미 보유한 아이템");
            }

            GameCharacter gameCharacter = characterRepository.findCharacterByIdx(goods.getCharacterIdx());
            // 아이템 장착 정보 초기화
            gameCharacter.setY(0);
            gameCharacter.setX(0);
            gameCharacter.setRotation(0);
            gameCharacter.setSize(0);
            gameCharacter.setGameItem(null);
            UserGameInfo owner = new UserGameInfo();
            owner.setUserId(user.getUserId());
            gameCharacter.setUserGameInfo(owner);
            characterRepository.save(gameCharacter);
        } else { // Item Type
            // 아이템 보유 여부 확인
            ItemName itemName = decoMapper.findNameById(goods.getItemIdx());
            ItemDao itemDao = new ItemDao();
            itemDao.setItemName(itemName);
            itemDao.setUserId(user.getUserId());
            int cnt = decoMapper.count(itemDao);

            if (cnt != 0) {// 이미 보유함 exception 발생
                log.debug("이미 보유한 아이템");
            }

            decoMapper.changeOwner(user.getUserId(), goods.getItemIdx());
        }

        // goods 판매 목록에서 삭제
        marketMapper.delete(buyReqDto.getGoodsIdx());
    }
}
