package com.shinhan.walfi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CharacterException extends RuntimeException {

    CharacterErrorCode characterErrorCode;

}
