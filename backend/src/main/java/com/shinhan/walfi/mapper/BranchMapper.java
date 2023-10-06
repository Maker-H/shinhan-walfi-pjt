package com.shinhan.walfi.mapper;

import com.shinhan.walfi.dao.BranchListDao;
import com.shinhan.walfi.dto.game.BranchListResDto;
import com.shinhan.walfi.dto.game.BranchDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BranchMapper {

    List<BranchListResDto> getBranches(BranchListDao dao);

    BranchDto getBranch(long id);
}
