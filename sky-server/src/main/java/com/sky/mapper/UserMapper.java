package com.sky.mapper;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface UserMapper {
    @Select("select * from sky_take_out.user where openid = #{openid}")
    User UserLogin(String openid);

    void insert(User user);
    @Select("select * from sky_take_out.user where id = #{userId}")
    User getById(Long userId);

    Integer getCount(Map map);
}
