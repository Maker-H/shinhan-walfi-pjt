package com.shinhan.walfi.repository.game;

import com.shinhan.walfi.domain.game.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    @Query(value = "select count(*) from branch where user_id = ?1", nativeQuery = true)
    int countOccupiedBranch(String userId);

    @Query(value ="select branch_name from branch where user_id = ?1 order by start_time limit 1;", nativeQuery = true)
    String getOldestStart(String userId);

}
