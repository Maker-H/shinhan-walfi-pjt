package com.shinhan.walfi.service.game;

import com.shinhan.walfi.dto.game.BranchListReqDto;
import com.shinhan.walfi.dto.game.BranchListResDto;
import com.shinhan.walfi.dto.game.BranchDto;
import com.shinhan.walfi.dto.game.BranchResDto;
import com.shinhan.walfi.dto.product.ProductResDto;

import java.util.List;

public interface BranchService {

    List<BranchListResDto> getBranches(BranchListReqDto branchListReqDto);

    BranchResDto getBranch(long idx);

    ProductResDto getCharacterBranchNum(String userId);
}
