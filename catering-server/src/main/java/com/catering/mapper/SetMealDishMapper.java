package com.catering.mapper;

import com.catering.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SetMealDishMapper {

    List<Long> setMealId(List<Long> ids);

    void insert(List<SetmealDish> setmealDishes);
    @Delete("delete from sky_take_out.setmeal_dish where setmeal_id = #{id}")
    void delete(Long id);
    @Select("select * from sky_take_out.setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> invertSetmealDish(Long id);
    @Select("select * from sky_take_out.setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> findDish(Long id);
}
