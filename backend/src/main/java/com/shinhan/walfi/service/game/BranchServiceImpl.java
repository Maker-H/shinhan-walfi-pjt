package com.shinhan.walfi.service.game;

import com.shinhan.walfi.dao.BranchListDao;
import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.domain.game.Branch;
import com.shinhan.walfi.dto.game.BranchListReqDto;
import com.shinhan.walfi.dto.game.BranchListResDto;
import com.shinhan.walfi.dto.game.BranchDto;
import com.shinhan.walfi.dto.game.BranchResDto;
import com.shinhan.walfi.dto.product.ProductResDto;
import com.shinhan.walfi.exception.BranchErrorCode;
import com.shinhan.walfi.exception.BranchException;
import com.shinhan.walfi.exception.UserErrorCode;
import com.shinhan.walfi.exception.UserException;
import com.shinhan.walfi.mapper.BranchMapper;
import com.shinhan.walfi.repository.UserRepository;
import com.shinhan.walfi.repository.game.BranchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchMapper branchMapper;

    private final BranchRepository branchRepository;

    private final UserRepository userRepository;

    @Override
    public List<BranchListResDto> getBranches(BranchListReqDto branchListReqDto) {

        // 입력 받은 위도 경도에 +- 1km 거리에 해당하는 지점 모두 조회하기

        // 입력 받은 위도, 경도의 +- 50m에 해당하는 값 계산
//        double latitudeDiff = 1 / (6371 * (Math.PI / 180)); // 미터 / ( 단위변환 * 지구반지름 * 라디안 )
        double latitudeDiff = 0.008993216059187306;
//        double longitudeDiff = 1 / (6371 * (Math.PI / 180) * Math.cos(Math.toRadians(35.8448)));
        double longitudeDiff = 0.011094433016828236;

        BranchListDao dao = new BranchListDao();
        dao.setMinLatitude(branchListReqDto.getLatitude()-latitudeDiff);
        dao.setPlusLatitude(branchListReqDto.getLatitude()+latitudeDiff);
        dao.setMinLongitude(branchListReqDto.getLongitude()-longitudeDiff);
        dao.setPlusLongitude(branchListReqDto.getLongitude()+longitudeDiff);
        List<BranchListResDto> branchList = branchMapper.getBranches(dao);

        return branchList;
    }

    @Override
    public BranchResDto getBranch(long id) {
        Optional<Branch> obranch = branchRepository.findById(id);

        if(!obranch.isPresent()){
            log.error("=== 브랜치 id:" + id + " 정보가 존재하지 않습니다 ===");
            throw new BranchException(BranchErrorCode.NO_MATCHING_BRANCH);
        }

        User user = userRepository.find(obranch.get().getUserGameInfo().getUserId());

        BranchDto branchDto = obranch.get().entityToDto();

        BranchResDto branchResDto = BranchResDto.builder()
                .username(user.getName())
                .userId(user.getUserId())
                .branchDto(branchDto)
                .build();

        log.info("=== 브랜치 id: " + id + " 조회 ===");
        return branchResDto;
    }

    /**
     * 3.7 ~ 4.5
     * @param userId
     * @return
     */
    @Override
    public ProductResDto getCharacterBranchNum(String userId) {
        User user = userRepository.find(userId);
        if (user == null) {
            log.error("=== id: " + user.getUserId() + " 틀린 비밀번호이거나 존재하지 않는 회원 ===");
            throw new UserException(UserErrorCode.NO_MATCHING_USER);
        }

        int occupiedBranch = branchRepository.countOccupiedBranch(userId);

        if (occupiedBranch == 0) {
            log.error("=== 해당 사용자는 지점장이 아닙니다 (가입불가) ===");
            throw new BranchException(BranchErrorCode.NOT_BRANCH_MANAGER);
        }

        if (occupiedBranch >= 8) {
            occupiedBranch = 8;
        }

        double 추가금리 = occupiedBranch * 0.1;
        double 총금리 = 추가금리 + 3.7;


        BigDecimal 변환총금리 = new BigDecimal(총금리);
        BigDecimal 변환추가금리 = new BigDecimal(추가금리);


        // 소수점 두 번째 자리까지 반올림
        변환총금리 = 변환총금리.setScale(2, RoundingMode.DOWN);
        변환추가금리 = 변환추가금리.setScale(2, RoundingMode.DOWN);

        return ProductResDto.builder()
                .상품명("지점장 정기예금")
                .가입기간("12")
                .기본금리("3.7")
                .추가금리(String.valueOf(변환추가금리))
                .총금리(String.valueOf(변환총금리))
                .build();

    }


}
