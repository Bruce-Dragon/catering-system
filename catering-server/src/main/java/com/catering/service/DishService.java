package com.catering.service;

import com.catering.dto.DishDTO;
import com.catering.dto.DishPageQueryDTO;
import com.catering.result.PageResult;
import com.catering.vo.DishVO;

import java.util.List;

public interface DishService {

    public PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 新增菜品
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 根据菜品id批量删除
     * @param id
     */
    void deleteById(List<Long> id);


    /**
     * 根据菜品id查询菜品数据，用于回显
     * @param id
     * @return
     */
    DishVO selectDishById(Long id);

    void updateDish(DishDTO dishDTO);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<DishVO> dishByCategoryId(Long categoryId);
}
