package com.shinhan.walfi.dto.game;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BranchResDto {

    private String userId;

    private String username;

    private BranchDto branchDto;

}
