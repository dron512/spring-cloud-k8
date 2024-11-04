package com.mh.userservice.repository;


import com.mh.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {

    UserEntity findByEmail(String username);
    UserEntity findByUserId(String UserId);
}
