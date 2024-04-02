package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DishFlavorMapper {


    /**
     * 新增口味 : 批量插入
     * @param flavors
     */
    @AutoFill(value = OperationType.INSERT)
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 删除菜品关联的口味
     * @param id
     */
    void deleteFlover(Long id);


    /**
     * 删除菜品关联的口味
     * @param id
     */
    void deleteFloverByIds(List<Long> id);
    /**
     * 根据菜品id查询口味数据，用于回显
     * @param dishId
     * @return
     */
    @Select("select * from sky_take_out.dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> selectDishFlavorById(Long dishId);

    /**
     * 根据菜品id查询口味数据
     * @param id
     * @return
     */
    @Select("select * from sky_take_out.dish_flavor where dish_id = #{id}")
    List<DishFlavor> flavorByCategoryId(Long id);
}
