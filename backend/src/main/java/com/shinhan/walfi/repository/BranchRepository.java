package com.shinhan.walfi.repository;

import com.shinhan.walfi.domain.game.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    @Query(value = "select count(*) from branch where user_id = ?1", nativeQuery = true)
    int countOccupiedBranch(String userId);
}