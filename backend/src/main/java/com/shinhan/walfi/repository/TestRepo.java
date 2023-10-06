package com.shinhan.walfi.repository;

import com.shinhan.walfi.domain.banking.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepo extends JpaRepository<TestEntity, Long> {
}
