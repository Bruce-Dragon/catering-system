package com.catering.mapper;

import com.github.pagehelper.Page;
import com.catering.dto.SetmealPageQueryDTO;
import com.catering.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SetmealMapper {
    /**
     * 根据分类id查询该分类关联套餐的数量
     * @param id
     * @return
     */
    @Select("select count(*) from sky_take_out.setmeal where category_id=#{id};")
    public Integer coutByCategoryId(Integer id);
    @Select("select * from sky_take_out.setmeal where category_id = #{categoryId}")
    List<Setmeal> setmeal(Long categoryId);
    /*List<Setmeal> list(Setmeal setmeal);*/

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<Setmeal> page(SetmealPageQueryDTO setmealPageQueryDTO);

    void insert(Setmeal setmeal);

    void update(Setmeal setmeal);
    @Select("select * from sky_take_out.setmeal where id = #{id}")
    Setmeal invertSetmeal(Long id);
}
