package com.shinhan.walfi.domain.banking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestEntity {

    @Id
    private Long id;

    private String 한글칼럼영어데이터;

    private String 한글칼럼한글데이터;

    private String engColumnEngData;

    private String engColumnKorData;
}
