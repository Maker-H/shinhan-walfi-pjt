package com.shinhan.walfi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Getter
@AllArgsConstructor
public enum CharacterErrorCode {

    HAS_MAIN_CHARACTER(BAD_REQUEST, "이미 메인 캐릭터가 있는 사용자입니다"),
    INFO_NO_MATCH(BAD_REQUEST, "해당 메인 캐릭터는 사용자의 캐릭터가 아닙니다"),
    THIS_IS_ALREADY_MAIN_CHRACTER(BAD_REQUEST, "해당 캐릭터는 이미 메인 캐릭터입니다"),
    CANNOT_RECOGNIZE(BAD_REQUEST, "보낸 스테이터스 명은 식별할 수 없는 문자입니다"),
    EAT_HAVE_TO_UPDATE_ATK(BAD_REQUEST, "밥을 먹으면 atk가 상승해야 합니다"),
    TRAINING_HAVE_TO_UPDATE_DEF(BAD_REQUEST, "훈련하기를 하면 def가 상승해야 합니다"),
    HAVE_TO_BE_PLUS(BAD_REQUEST, "값을 하락시킬 수 없습니다");

    private final HttpStatus httpStatus;

    private final String message;
}
