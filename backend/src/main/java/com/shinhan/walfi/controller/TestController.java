package com.shinhan.walfi.controller;

import com.shinhan.walfi.domain.banking.TestEntity;
import com.shinhan.walfi.dto.TestEntityDTO;
import com.shinhan.walfi.mapper.BankMapper;
import com.shinhan.walfi.repository.TestRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TestRepo testRepo;
    private final BankMapper bankMapper;

    @GetMapping
    public String test(){
        return "서버 작동 중!";
    }

    @GetMapping("/test")
    public TestEntity test1() {

        TestEntity testEntity = new TestEntity(
                1L,
                "english",
                "한글데이터",
                "english_data",
                "한글데이터"
        );

        testRepo.save(testEntity);

        return testRepo.findById(1L).get();
    }

    @GetMapping("/testa")
    public TestEntityDTO test2() {
        bankMapper.testAddData();
        TestEntityDTO testEntityDTO = bankMapper.testLoadData();
        return testEntityDTO;
    }
}
