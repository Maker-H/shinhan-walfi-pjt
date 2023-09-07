package com.shinhan.walfi.controller;

import com.shinhan.walfi.domain.HttpResult;
import com.shinhan.walfi.dto.game.BranchListReqDto;
import com.shinhan.walfi.dto.game.BranchListResDto;
import com.shinhan.walfi.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/branch")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<HttpResult> getBranches(@RequestBody BranchListReqDto branchListReqDto){

        BranchListResDto branchListResDto = branchService.getBranches(branchListReqDto);

        HttpResult res;
        res = HttpResult.getSuccess();
        res.setData(branchListResDto);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}