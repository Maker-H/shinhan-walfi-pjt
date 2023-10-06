package com.shinhan.walfi.repository;

import com.shinhan.walfi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

//    @Query(value = "select * from user where user_id = ?1 and password= ?2", nativeQuery = true)
//    User login(String userId, String password);

    @Query(value = "select * from user where user_id = ?1", nativeQuery = true)
    User find(String userId);

    @Query(value = "select * from user", nativeQuery = true)
    List<User> findAll();

    @Query(value = "select count(user_id) from user where user_id = ?1", nativeQuery = true)
    int countId(String userId);

    @Query(value = "select 대표계좌 from user order by 대표계좌 desc limit 1", nativeQuery = true)
    String findLastMainNum();
}
