package com.shinhan.walfi.service.game;

import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.domain.banking.CryptoWallet;
import com.shinhan.walfi.domain.enums.CoinType;
import com.shinhan.walfi.domain.game.UserGameInfo;
import com.shinhan.walfi.dto.game.UserGameInfoDto;
import com.shinhan.walfi.exception.UserException;
import com.shinhan.walfi.repository.UserRepository;
import com.shinhan.walfi.repository.banking.CryptoWalletRepository;
import com.shinhan.walfi.repository.game.UserGameInfoRepository;
import com.shinhan.walfi.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.shinhan.walfi.exception.UserErrorCode.NO_MATCHING_USER;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserGameInfoServiceImpl implements UserGameInfoService{

    private final UserGameInfoRepository userGameInfoRepository;

    private final UserRepository userRepository;

    private final CryptoWalletRepository cryptoWalletRepository;

    private final CryptoUtil cryptoUtil;

    /**
     * 유저의 게임 포인트, 게임 내 타이틀, 이더 잔액을 반환하는 기능
     *
     * @exception 'NO_MATCHING_USER' - 존재하지 않는 사용자의 경우 예외 발생
     * @param userId
     * @return UserGameInfoDto
     */
    @Override
    public UserGameInfoDto getUserGameInfo(String userId) {

        User user = userRepository.find(userId);

        UserGameInfo findUserGameInfo = userGameInfoRepository.findById(userId);

        if (findUserGameInfo == null || user == null) {
            log.error("=== 틀린 비밀번호이거나 존재하지 않는 회원 ===");
            throw new UserException(NO_MATCHING_USER);
        }

        CryptoWallet wallet = cryptoWalletRepository.findWallet(user.get대표계좌(), CoinType.SEP);
        String ethBalance = cryptoUtil.checkBalance(wallet.getAddress());

        return  UserGameInfoDto.getUserGameInfoDto(findUserGameInfo, user.getName(), ethBalance);

    }

    /**
     * 유저의 게임 포인트를 상승 시키는 기능
     *
     * @exception 'NO_MATCHING_USER' - 존재하지 않는 사용자의 경우 예외 발생
     * @param userId
     * @param updatePoint
     * @return UserGameInfoDto
     */
    @Override
    @Transactional
    public UserGameInfoDto updatePoint(String userId, int updatePoint) {

        UserGameInfo findUserGameInfo = userGameInfoRepository.findById(userId);

        if (findUserGameInfo == null) {
            log.error("=== 틀린 비밀번호이거나 존재하지 않는 회원 ===");
            throw new UserException(NO_MATCHING_USER);
        }

        int defaultPoint = findUserGameInfo.getPoint();
        int newPoint = defaultPoint + updatePoint;

        findUserGameInfo.setPoint(newPoint);
        UserGameInfo updateUserGameInfo = userGameInfoRepository.save(findUserGameInfo);

        log.info("=== " + userId + "사용자: 포인트 " + defaultPoint + " -> " + newPoint + "===");
        return UserGameInfoDto.getUserGameInfoDto(updateUserGameInfo);
    }


}
