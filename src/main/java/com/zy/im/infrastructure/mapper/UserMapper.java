package com.zy.im.infrastructure.mapper;

import com.zy.im.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{username}")
    User findByUsername(@Param("username") String username);

    /**
     * 判断用户是否存在
     */
    @Select("""
        SELECT 1
        FROM user
        WHERE uuid = #{uuid}
        LIMIT 1
    """)
    Integer existsByUuid(@Param("uuid") String uuid);

    @Insert("""
    INSERT INTO user (
        uuid,
        username,
        password,
        status
    ) VALUES (
        #{uuid},
        #{username},
        #{password},
        #{status}
        )
    """)
    Integer insertUser(User user);
}
