package com.shinhan.walfi.dto.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchDto {

    private Long 지점번호;

    private String 지점명;

    private String 지점주소;

    private String 지점대표전화번호;

    private double 지점위도;

    private double 지점경도;

    private String managerAnimalType;

    private String managerAnimalColor;

    private int managerLevel;

    private int managerExp;

    private int managerHp;

    private int managerAtk;

    private int managerDef;

    public BranchDto(Long 지점번호, String 지점명, String 지점주소, String 지점대표전화번호, double 지점위도, double 지점경도, String managerAnimalType, String managerAnimalColor, int managerLevel, int managerExp, int managerHp, int managerAtk, int managerDef) {
        this.지점번호 = 지점번호;
        this.지점명 = 지점명;
        this.지점주소 = 지점주소;
        this.지점대표전화번호 = 지점대표전화번호;
        this.지점위도 = 지점위도;
        this.지점경도 = 지점경도;
        this.managerAnimalType = managerAnimalType;
        this.managerAnimalColor = managerAnimalColor;
        this.managerLevel = managerLevel;
        this.managerExp = managerExp;
        this.managerHp = managerHp;
        this.managerAtk = managerAtk;
        this.managerDef = managerDef;
    }
}
