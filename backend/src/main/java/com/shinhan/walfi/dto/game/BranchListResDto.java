package com.shinhan.walfi.dto.game;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
public class BranchListResDto {

    @Id
    private Long 지점번호;

    private String 지점명;

    private String 지점주소;

    private String 지점대표전화번호;

    private double 지점위도;

    private double 지점경도;

}
