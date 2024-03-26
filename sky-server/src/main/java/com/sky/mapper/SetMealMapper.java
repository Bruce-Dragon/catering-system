package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SetMealMapper {
    /**
     * 根据分类id查询该分类关联套餐的数量
     * @param id
     * @return
     */
    @Select("select count(*) from sky_take_out.setmeal where category_id=#{id};")
    public Integer coutByCategoryId(Integer id);
}
