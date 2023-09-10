package com.shinhan.walfi.service;

import com.shinhan.walfi.domain.CharacterType;
import com.shinhan.walfi.domain.TierPerColor;
import com.shinhan.walfi.domain.game.GameCharacter;
import com.shinhan.walfi.domain.game.UserGameInfo;
import com.shinhan.walfi.dto.game.CharacterDto;
import com.shinhan.walfi.dto.game.CharacterListResDto;
import com.shinhan.walfi.dto.game.CharacterWithUserIdResDto;
import com.shinhan.walfi.repository.CharacterRepository;
import com.shinhan.walfi.repository.UserGameInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;
    private final UserGameInfoRepository userGameInfoRepository;

    /**
     * 캐릭터 타입 랜덤 설정 후 캐릭터 생성
     *
     * @param userId
     * @return 생성한 캐릭터 idx 반환
     */
    @Override
    @Transactional
    public Long create(String userId) {
        UserGameInfo userGameInfo = userGameInfoRepository.findById(userId);


        // TODO: 만약 메인 캐릭터가 존재하면 create가 아닌 shop으로 캐릭터를 뽑아야 함으로 예외처리

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

        return gameCharacter.getCharacterIdx();
    }

    /**
     * 캐릭터 뽑기
     *
     * @param userId
     * @return 뽑은 캐릭터 idx 반환
     */
    @Override
    @Transactional
    public Long shop(String userId) {
        UserGameInfo userGameInfo = userGameInfoRepository.findById(userId);

        Random random = new Random();

        // 캐릭터 타입 랜덤 생성
        CharacterType[] characterTypes = CharacterType.values();
        int typesRandomNum = random.nextInt(characterTypes.length);
        CharacterType randomCharacterType = characterTypes[typesRandomNum];

        // 캐릭터 생성
        Boolean isMain = false;
        GameCharacter gameCharacter = GameCharacter.createCharacter(userGameInfo, randomCharacterType, isMain);

        // db에 저장
        characterRepository.save(gameCharacter);

        return gameCharacter.getCharacterIdx();
    }

    /**
     * 사용자가 가진 전체 캐릭터 조회
     *
     * @param userId
     * @return 캐릭터 DTO
     */
    @Override
    public CharacterListResDto searchCharacters(String userId) {
        UserGameInfo userGameInfo = userGameInfoRepository.findById(userId);

        List<GameCharacter> characters = characterRepository.findCharacters(userGameInfo);

        List<CharacterDto> dtoList = characters.stream()
                .map(character -> getCharacterDto(character))
                .collect(Collectors.toList());

        CharacterListResDto characterListResDto = CharacterListResDto.builder()
                .characterDtoList(dtoList)
                .userId(userId)
                .build();

        return characterListResDto;
    }

    /**
     * 사용자의 메인 캐릭터 조회
     *
     * @param userId
     * @return CharacterWithUserIdResDto
     */
    @Override
    public CharacterWithUserIdResDto searchMainCharacter(String userId) {
        UserGameInfo userGameInfo = userGameInfoRepository.findById(userId);
        GameCharacter mainCharacter = characterRepository.findMainCharacter(userGameInfo);

        CharacterDto characterDto = getCharacterDto(mainCharacter);

        CharacterWithUserIdResDto characterWithUserIdResDto = getCharacterWithUserIdResDto(userId, characterDto);

        return characterWithUserIdResDto;
    }

    /**
     *  사용자의 메인 캐릭터 색을 변경하고 캐릭터 정보를 반환
     *
     * @param userId
     * @param mainCharacterIdx
     * @return CharacterWithUserIdResDto
     */
    @Override
    @Transactional
    public CharacterWithUserIdResDto changeCharacterColor(String userId, Long mainCharacterIdx) {
        UserGameInfo userGameInfo = userGameInfoRepository.findById(userId);
        GameCharacter mainCharacter = characterRepository.findMainCharacter(userGameInfo);

        if (mainCharacter.getCharacterIdx() != mainCharacterIdx) {
            // TODO: 전송한 캐릭터가 사용자의 main 캐릭터가 아닐 시 예외 처리
        }

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

        return characterWithUserIdResDto;
    }

    /**
     * 캐릭터의 스텟 변경하는 기능
     *
     * @param userId
     * @param characterIdx
     * @param statusType
     * @param statusValue
     * @return CharacterWithUserIdResDto
     */
    @Override
    @Transactional
    public CharacterWithUserIdResDto changeCharacterStatus(String userId,
                                                           Long characterIdx,
                                                           String statusType,
                                                           int statusValue) {

        UserGameInfo userGameInfo = userGameInfoRepository.findById(userId);
        GameCharacter character = characterRepository.findCharacterByIdx(characterIdx);

        if (character.getCharacterIdx() != characterIdx) {
            // TODO: 전송한 캐릭터가 사용자의 캐릭터가 아닐 시 예외 처리
        }

        // atk, def, hp, exp(레벨업 로직), isMain(메인 캐릭터인거 아니게 바꾸는 로직 포함)
        if (statusType.equals("atk")) {
            int defaultAtk = character.getAtk();
            character.setAtk(defaultAtk + statusValue);
        } else if (statusType.equals("def")) {
            int defaultDef = character.getDef();
            character.setDef(defaultDef + statusValue);
        } else if (statusType.equals("hp")) {
            int defaultHp = character.getHp();
            character.setHp(defaultHp + statusValue);
        } else if (statusType.equals("exp")) {

        } else if (statusType.equals("isMain")) {
            if (character.isMain()) {
                // TODO: 사용자의 캐릭터가 이미 메인 캐릭터임으로 변경할 수 없다는 예외 발생
            }

            // 기존 메인 캐릭터를 메인이 아니게 변경
            GameCharacter mainCharacter = characterRepository.findMainCharacter(userGameInfo);
            mainCharacter.setMain(false);
            characterRepository.save(mainCharacter);

            // 전송한 캐릭터를 메인 캐릭터로 변경
            character.setMain(true);
        } else {
            // TODO: 전송한 스테이터스를 알 수 없을 때 예외 발생
        }

        // 변경한 스텟 반영하여 저장
        GameCharacter saveGameCharacter = characterRepository.save(character);

        // dto로 변환
        CharacterDto characterDto = getCharacterDto(saveGameCharacter);
        CharacterWithUserIdResDto characterWithUserIdResDto = getCharacterWithUserIdResDto(userId, characterDto);

        return characterWithUserIdResDto;
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
                .level(gameCharacter.getLevel())
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

}