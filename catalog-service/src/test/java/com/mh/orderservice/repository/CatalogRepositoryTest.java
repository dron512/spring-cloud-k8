package com.mh.orderservice.repository;

import com.mh.catalogservice.repository.CatalogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CatalogRepositoryTest {

    @Autowired
    private CatalogRepository userRepository;

    @Test
    @Transactional
    @Rollback
    @DisplayName("mybatis 회원가입 테스트")
    public void memberSaveTest1() {
//        userRepository.save(new UserEntity(null, "mybatis회원이메일", "mybatis회원비밀번호", "mybatis회원이름"));
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("mybatis 회원가입 테스트 mapper X")
    public void memberSaveTest2() {
//        userRepository.save2(new UserEntity(null, "mybatis회원이메일", "mybatis회원비밀번호", "mybatis회원이름"));
    }
}