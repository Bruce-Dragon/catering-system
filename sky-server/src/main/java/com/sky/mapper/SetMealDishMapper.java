package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SetMealDishMapper {

    List<Long> setMealId(List<Long> ids);

}
