package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DishMapper {

    /**
     * 根据分类id查询该分类关联菜品的数量
     * @param id
     * @return
     */
    @Select("select count(*) from sky_take_out.dish where category_id=#{id};")
    public Integer coutCategoryById(Integer id);

    public Page<DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 新增菜品
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);
    /**
     * 根据菜品id批量删除
     * @param id
     */
    //void delete(Long id);


    /**
     * 根据菜品id批量删除
     * @param id
     */
    void deleteByIds(List<Long> id);

    /**
     * 根据菜品id查询菜品售卖状态
     * @param id
     * @return
     */
    List<Dish> findStatus(List<Long> id);

    /**
     * 根据菜品id查询菜品数据用于回显
     * @param id
     */
    Dish selectDishById(Long id);
    @AutoFill(value = OperationType.UPDATE)
    void updateDish(Dish dish);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Select("select * from sky_take_out.dish where category_id = #{categoryId}")
    List<DishVO> categoryId(Long categoryId);
}
