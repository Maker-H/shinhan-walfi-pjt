package com.shinhan.walfi.service.game;

import com.shinhan.walfi.domain.enums.CharacterType;
import com.shinhan.walfi.domain.enums.TierPerColor;
import com.shinhan.walfi.domain.game.GameCharacter;
import com.shinhan.walfi.domain.game.UserGameInfo;
import com.shinhan.walfi.dto.game.CharacterDto;
import com.shinhan.walfi.dto.game.CharacterListResDto;
import com.shinhan.walfi.dto.game.CharacterWithUserIdResDto;
import com.shinhan.walfi.dto.product.ProductResDto;
import com.shinhan.walfi.exception.CharacterErrorCode;
import com.shinhan.walfi.exception.CharacterException;
import com.shinhan.walfi.exception.UserErrorCode;
import com.shinhan.walfi.exception.UserException;
import com.shinhan.walfi.repository.game.CharacterRepository;
import com.shinhan.walfi.repository.game.UserGameInfoRepository;
import com.shinhan.walfi.util.CharacterStatusUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;

    private final UserGameInfoRepository userGameInfoRepository;

    private final CharacterStatusUtil util;

    /**
     * 캐릭터 타입 랜덤 설정 후 캐릭터 생성
     *
     * @exception 'NO_MATCHING_USER' - 비밀번호가 틀리거나 존재하지 않는 사용자의 경우 예외 발생
     * @exception 'HAS_MAIN_CHARACTER' - 메인 캐릭터가 존재하는 유저의 경우 뽑기 기능을 사용해야 하므로 예외 발생
     * @param userId
     * @return 생성한 캐릭터 idx 반환
     */
    @Override
    @Transactional
    public CharacterWithUserIdResDto create(String userId) {
        UserGameInfo userGameInfo = userGameInfoRepository.findById(userId);
        checkExistUser(userGameInfo);

        boolean hasMain = userGameInfo.getGameCharacters().stream()
                .anyMatch(character -> character.isMain() == true);

        if (hasMain) {
            log.error("=== 메인 캐릭터가 존재하는 회원, 캐릭터 뽑기 기능 사용해야함 ===");
            throw new CharacterException(CharacterErrorCode.HAS_MAIN_CHARACTER);
        }

        Random random = new Random();

        // 캐릭터 타입 랜덤 생성
        CharacterType[] characterTypes = CharacterType.values();
        int typesRandomNum = random.nextInt(characterTypes.length);
        CharacterType randomCharacterType = characterTypes[typesRandomNum];

        // 캐릭터 생성
        Boolean isMain = true;
        GameCharacter gameCharacter = GameCharacter.createCharacter(userGameInfo, randomCharacterType, isMain);

        // db에 저장
        characterRepository.save(gameCharacter);

        CharacterDto characterDto = getCharacterDto(gameCharacter);
        CharacterWithUserIdResDto characterWithUserIdResDto = getCharacterWithUserIdResDto(userId, characterDto);

        log.info("=== 사용자: " + userId + "의 캐릭터가 생성되었습니다 ===");
        return characterWithUserIdResDto;
    }

    /**
     * 캐릭터 뽑기 (이미 존재하는 캐릭터 뽑을 경우 공방 + 1 상승)
     *
     * @exception 'NO_MATCHING_USER' - 비밀번호가 틀리거나 존재하지 않는 사용자의 경우 예외 발생
     * @param userId
     * @return 뽑은 캐릭터 idx 반환
     */
    @Override
    @Transactional
    public CharacterWithUserIdResDto shop(String userId) {
        // 검증
        UserGameInfo userGameInfo = userGameInfoRepository.findById(userId);
        checkExistUser(userGameInfo);


        // 캐릭터 타입 랜덤 생성
        Random random = new Random();

        CharacterType[] characterTypes = CharacterType.values();
        int typesRandomNum = random.nextInt(characterTypes.length);
        CharacterType randomCharacterType = characterTypes[typesRandomNum];

        List<GameCharacter> characters = userGameInfo.getGameCharacters();

        Optional<GameCharacter> matchingCharacter = characters.stream()
                .filter(character -> character.getCharacterType().equals(randomCharacterType))
                .findFirst();

        if (matchingCharacter.isPresent()) {
            GameCharacter character = matchingCharacter.get();
            updateCharacterStatus(userId, character.getCharacterIdx(), "atk", 1, "");
            updateCharacterStatus(userId, character.getCharacterIdx(), "def", 1, "");

            return getCharacterWithUserIdResDto(userId, getCharacterDto(character));
        }

        // 캐릭터 생성
        Boolean isMain = false;
        GameCharacter gameCharacter = GameCharacter.createCharacter(userGameInfo, randomCharacterType, isMain);

        // db에 저장
        characterRepository.save(gameCharacter);

        CharacterDto characterDto = getCharacterDto(gameCharacter);
        CharacterWithUserIdResDto characterWithUserIdResDto = getCharacterWithUserIdResDto(userId, characterDto);

        log.info("=== 사용자: " + userId + "가 캐릭터 뽑기를 동작시켰습니다 ===");
        return characterWithUserIdResDto;
    }

    /**
     * 캐릭터를 열 개 연속으로 뽑는 기능
     *
     * @exception 'NO_MATCHING_USER' - 비밀번호가 틀리거나 존재하지 않는 사용자의 경우 예외 발생
     * @param userId
     * @return CharacterListResDto
     */
    @Override
    @Transactional
    public CharacterListResDto shopTen(String userId) {
        // 검증
        UserGameInfo userGameInfo = userGameInfoRepository.findById(userId);
        checkExistUser(userGameInfo);

        Random random = new Random();
        List<CharacterDto> characterDtos = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            // 캐릭터 타입 랜덤 생성
            CharacterType[] characterTypes = CharacterType.values();
            int typesRandomNum = random.nextInt(characterTypes.length);
            CharacterType randomCharacterType = characterTypes[typesRandomNum];

            // 가지고 있는 게임 캐릭터랑 겹치는지 검증
            List<GameCharacter> characters = userGameInfo.getGameCharacters();
            Optional<GameCharacter> matchingCharacter = characters.stream()
                    .filter(character -> character.getCharacterType().equals(randomCharacterType))
                    .findFirst();

            if (matchingCharacter.isPresent()) {
                GameCharacter character = matchingCharacter.get();
                updateCharacterStatus(userId, character.getCharacterIdx(), "atk", 1, "");
                updateCharacterStatus(userId, character.getCharacterIdx(), "def", 1, "");

                characterDtos.add(getCharacterDto(character));
                continue;
            }

            // 캐릭터 생성
            Boolean isMain = false;
            GameCharacter gameCharacter = GameCharacter.createCharacter(userGameInfo, randomCharacterType, isMain);

            // db에 저장
            characterRepository.save(gameCharacter);
            characterDtos.add(getCharacterDto(gameCharacter));
        }

        return getCharacterListResDto(userId, characterDtos);
    }

    /**
     * 사용자가 가진 전체 캐릭터 조회
     *
     * @exception 'NO_MATCHING_USER' - 비밀번호가 틀리거나 존재하지 않는 사용자의 경우 예외 발생
     * @param userId
     * @return 캐릭터 DTO
     */
    @Override
    public CharacterListResDto searchCharacters(String userId) {
        UserGameInfo userGameInfo = userGameInfoRepository.findById(userId);
        checkExistUser(userGameInfo);

        List<GameCharacter> characters = userGameInfo.getGameCharacters();

        List<CharacterDto> dtoList = characters.stream()
                .map(character -> getCharacterDto(character))
                .collect(Collectors.toList());

        CharacterListResDto characterListResDto = getCharacterListResDto(userId, dtoList);

        log.info("=== 사용자: " + userId + "의 캐릭터 전체 조회 ===");
        return characterListResDto;
    }

    /**
     * 사용자의 메인 캐릭터 조회
     *
     * @exception 'NO_MATCHING_USER' - 비밀번호가 틀리거나 존재하지 않는 사용자의 경우 예외 발생
     * @param userId
     * @return CharacterWithUserIdResDto
     */
    @Override
    public CharacterWithUserIdResDto searchMainCharacter(String userId) {
        UserGameInfo userGameInfo = userGameInfoRepository.findById(userId);
        checkExistUser(userGameInfo);

        GameCharacter mainCharacter = characterRepository.findMainCharacter(userGameInfo);
        checkMainCharacterExist(userId, mainCharacter.getCharacterIdx(), mainCharacter);

        CharacterDto characterDto = getCharacterDto(mainCharacter);

        CharacterWithUserIdResDto characterWithUserIdResDto = getCharacterWithUserIdResDto(userId, characterDto);

        log.info("=== 사용자: " + userId + "의 메인 캐릭터 조회 ===");
        return characterWithUserIdResDto;
    }

    /**
     *  사용자의 메인 캐릭터 색을 변경하고 캐릭터 정보를 반환
     *
     * @exception 'NO_MATCHING_USER' - 비밀번호가 틀리거나 존재하지 않는 사용자의 경우 예외 발생
     * @exception 'INFO_NO_MATCH' - 해당 메인 캐릭터가 사용자의 캐릭터가 아닐시 예외 발생
     * @param userId
     * @param mainCharacterIdx
     * @return CharacterWithUserIdResDto
     */
    @Override
    @Transactional
    public CharacterWithUserIdResDto changeCharacterColor(String userId, Long mainCharacterIdx) {
        UserGameInfo userGameInfo = userGameInfoRepository.findById(userId);
        checkExistUser(userGameInfo);

        GameCharacter mainCharacter = characterRepository.findMainCharacter(userGameInfo);
        checkMainCharacterExist(userId, mainCharacterIdx, mainCharacter);

        // 랜덤으로 색 뽑기 로직
        Random random = new Random();
        double rand = random.nextDouble() * 100; // 0 ~ 100

        double basicPercent = TierPerColor.BASIC.getGrade().getPercent();
        double epicPercent = TierPerColor.WHITE.getGrade().getPercent() + basicPercent;
        double uniquePercent = TierPerColor.MINT.getGrade().getPercent() + epicPercent;
        double legendPercent = TierPerColor.LED.getGrade().getPercent() + uniquePercent;

        if (rand < basicPercent) { // 0 ~ 1.9999
            mainCharacter.setColor(TierPerColor.BASIC);
        } else if (rand < epicPercent) { // 2 ~ 6.9999
            mainCharacter.setColor(TierPerColor.WHITE);
        } else if (rand < uniquePercent) { // 7 ~ 99.998
            mainCharacter.setColor(TierPerColor.MINT);
        } else if (rand <= legendPercent) { // 99.999 ~ 100
            mainCharacter.setColor(TierPerColor.LED);
        }

        // 변경한 색 반영하여 저장
        GameCharacter saveGameCharacter = characterRepository.save(mainCharacter);

        // dto로 변환
        CharacterDto characterDto = getCharacterDto(saveGameCharacter);
        CharacterWithUserIdResDto characterWithUserIdResDto = getCharacterWithUserIdResDto(userId, characterDto);

        log.info("=== 사용자: " + userId + "의 idx: " + mainCharacterIdx + "의 색상 뽑기를 동작하였습니다 ===");
        return characterWithUserIdResDto;
    }



    /**
     * 캐릭터의 스텟 변경하는 기능
     *
     * @exception 'NO_MATCHING_USER' - 비밀번호가 틀리거나 존재하지 않는 사용자의 경우 예외 발생
     * @exception 'INFO_NO_MATCH' - 해당 메인 캐릭터가 사용자의 캐릭터가 아닐시 예외 발생
     * @exception 'HAVE_TO_BE_PLUS' - 능력치를 하락시키려고 할 때 예외 발생
     * @exception 'EAT_HAVE_TO_UPDATE_ATK' - 밥먹기가 atk를 상승시키지 않을 때 예외 발생
     * @exception 'TRAINING_HAVE_TO_UPDATE_DEF' - 훈련하기가 def를 상승키지 않을 때 예외 발생
     * @exception 'THIS_IS_ALREADY_MAIN_CHRACTER' - isMain과 함께 전송한 캐릭터가 이미 메인 캐릭터인 경우 예외 발생
     * @exception 'CANNOT_RECOGNIZE' - 식보낸 statusType을 식별할 수 없을 때 예외 발생
     * @param userId
     * @param characterIdx
     * @param statusType
     * @param statusValue
     * @return CharacterWithUserIdResDto
     */
    @Override
    @Transactional
    public CharacterWithUserIdResDto updateCharacterStatus(String userId,
                                                           Long characterIdx,
                                                           String statusType,
                                                           int statusValue,
                                                           String act) {

        UserGameInfo userGameInfo = userGameInfoRepository.findById(userId);
        checkExistUser(userGameInfo);

        GameCharacter character = characterRepository.findCharacterByIdx(characterIdx);
        checkMainCharacterExist(userId, characterIdx, character);

        if (act.equals("밥먹기") && !statusType.equals("atk") && statusValue != 1) {
            log.error("=== 밥먹기로 atk를 상승시켜야 합니다 ===");
            throw new CharacterException(CharacterErrorCode.EAT_HAVE_TO_UPDATE_ATK);
        }

        if (act.equals("훈련하기") && !statusType.equals("def") && statusValue != 1) {
            log.error("=== 훈련하기로 def를 상승시켜야 합니다 ===");
            throw new CharacterException(CharacterErrorCode.TRAINING_HAVE_TO_UPDATE_DEF);
        }

        // atk, def, hp, exp(레벨업 로직), isMain(메인 캐릭터인걸 아니게 바꾸는 로직 포함)
        switch (statusType) {
            case "atk":
                int defaultAtk = character.getAtk();
                character.setAtk(defaultAtk + statusValue);
                break;

            case "def":
                int defaultDef = character.getDef();
                character.setDef(defaultDef + statusValue);
                break;

            case "hp":
                int defaultHp = character.getHp();
                character.setHp(defaultHp + statusValue);
                break;

            case "exp":
                character = util.updateExp(statusValue, character);
                break;

            case "isMain":
                if (character.isMain()) {
                    log.error("=== ("+ characterIdx + ") 메인 캐릭터는 이미 메인 캐릭터임으로 예외가 발생함 ===");
                    throw new CharacterException(CharacterErrorCode.THIS_IS_ALREADY_MAIN_CHRACTER);
                }

                // 기존 메인 캐릭터를 메인이 아니게 변경
                GameCharacter mainCharacter = characterRepository.findMainCharacter(userGameInfo);
                mainCharacter.setMain(false);
                characterRepository.save(mainCharacter);

                // 전송한 캐릭터를 메인 캐릭터로 변경
                character.setMain(true);
                break;

            default:
                log.error("=== <"+ statusType + "> 은 해당 기능에서 인식할 수 없는 문자입니다 ===");
                throw new CharacterException(CharacterErrorCode.CANNOT_RECOGNIZE);

        }

        // 변경한 스텟 반영하여 저장
        GameCharacter saveGameCharacter = characterRepository.save(character);

        // dto로 변환
        CharacterDto characterDto = getCharacterDto(saveGameCharacter);
        CharacterWithUserIdResDto characterWithUserIdResDto = getCharacterWithUserIdResDto(userId, characterDto);

        log.info("=== 사용자: " + userId + " 의 " + statusType + "가 +" + statusValue + "상승하였습니다 ===");
        return characterWithUserIdResDto;
    }


    @Transactional
    public CharacterWithUserIdResDto updateCharacterStatus(String userId,
                                                           Long characterIdx,
                                                           String statusType,
                                                           int statusValue) {
        return updateCharacterStatus(userId, characterIdx, statusType, statusValue, "");
    }

    /**
     * 맥스 레벨인 캐릭터 수를 반환하는 기능
     *
     * @exception 'NO_MATCHING_USER' - 비밀번호가 틀리거나 존재하지 않는 사용자의 경우 예외 발생
     * @param userId
     * @return MaxCharacterNumResDto
     */
    @Override
    public ProductResDto getMaxLevelCharacterNum(String userId) {
        UserGameInfo userGameInfo = userGameInfoRepository.findById(userId);
        checkExistUser(userGameInfo);

        int maxLevelCharacterNum = characterRepository.findMaxLevelCharacterNum(userGameInfo);

        double 추가금리 = maxLevelCharacterNum * 0.1;
        double 총금리 = 추가금리 + 3.7;

        BigDecimal 변환총금리 = new BigDecimal(총금리);
        BigDecimal 변환추가금리 = new BigDecimal(추가금리);


        // 소수점 두 번째 자리까지 반올림
        변환총금리 = 변환총금리.setScale(2, RoundingMode.DOWN);
        변환추가금리 = 변환추가금리.setScale(2, RoundingMode.DOWN);

        ProductResDto productResDto = ProductResDto.builder()
                .총금리(String.valueOf(변환총금리))
                .추가금리(String.valueOf(변환추가금리))
                .가입기간("12")
                .기본금리("3.7")
                .상품명("LevelUp 정기예금")
                .build();

        return productResDto;
    }

    @Override
    public void setFirstMain(Long characterIdx) {
        GameCharacter character = characterRepository.findCharacterByIdx(characterIdx);
        character.setMain(true);
    }

    /**
     * GameCharacter를 CharacterDto로 변환하는 기능
     *
     * @param gameCharacter
     * @return CharacterDto
     */
    private CharacterDto getCharacterDto(GameCharacter gameCharacter) {
        return CharacterDto.builder()
                .characterIdx(gameCharacter.getCharacterIdx())
                .characterType(gameCharacter.getCharacterType())
                .color(gameCharacter.getColor())
                .level(gameCharacter.getLevel().getLevel())
                .exp(gameCharacter.getExp())
                .hp(gameCharacter.getHp())
                .atk(gameCharacter.getAtk())
                .def(gameCharacter.getDef())
                .isMain(gameCharacter.isMain())
                .createdTime(gameCharacter.getCreatedTime())
                .build();
    }

    /**
     * CharacterDto를 CharacterWithUserIdResDto로 변환하는 기능
     *
     * @param userId
     * @param characterDto
     * @return CharacterWithUserIdResDto
     */
    private CharacterWithUserIdResDto getCharacterWithUserIdResDto(String userId, CharacterDto characterDto) {
        CharacterWithUserIdResDto characterWithUserIdResDto = CharacterWithUserIdResDto.builder()
                .userId(userId)
                .characterDto(characterDto)
                .build();
        return characterWithUserIdResDto;
    }

    private static CharacterListResDto getCharacterListResDto(String userId, List<CharacterDto> dtoList) {
        CharacterListResDto characterListResDto = CharacterListResDto.builder()
                .characterDtoList(dtoList)
                .userId(userId)
                .build();
        return characterListResDto;
    }

//    private Character


    private static void checkExistUser(UserGameInfo userGameInfo) {
        if (userGameInfo == null) {
            log.error("=== id: " + userGameInfo.getUserId() + " 틀린 비밀번호이거나 존재하지 않는 회원 ===");
            throw new UserException(UserErrorCode.NO_MATCHING_USER);
        }
    }

    private static void checkMainCharacterExist(String userId, Long mainCharacterIdx, GameCharacter mainCharacter) {
        if (mainCharacter.getCharacterIdx() != mainCharacterIdx) {
            log.error("=== ("+ mainCharacterIdx + ") 메인 캐릭터는 사용자("+ userId +")의 캐릭터가 아닙니다 ===");
            throw new CharacterException(CharacterErrorCode.INFO_NO_MATCH);
        }
    }
}
