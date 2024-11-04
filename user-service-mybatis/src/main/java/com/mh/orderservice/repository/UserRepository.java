package com.mh.orderservice.repository;

import com.mh.orderservice.dto.UserMapperDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserRepository {

    void save(UserMapperDto userMapperDto);

    @Insert("insert into users(user_email, user_password, user_name) " +
            "values(#{user_email}, #{user_password}, #{user_name})")
    void save2(UserMapperDto userMapperDto);

    @Select("select * from users where user_email = #{username}")
    UserMapperDto findByEmail(String username);

    @Select("select * from users")
    Iterable<UserMapperDto> findAll();

}
