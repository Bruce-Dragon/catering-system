package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DIshServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.page(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 新增菜品
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);
        Long id = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() != 0){
            flavors.forEach(dishFlavor ->
                    dishFlavor.setDishId(id));
            dishFlavorMapper.insertBatch(flavors);
        }
    }
    /**
     * 根据菜品id批量删除
     * @param id
     */
    @Override
    public void deleteById(List<Long> id) {
        //判断菜品状态是否是启售中
        List<Dish> status = dishMapper.findStatus(id);
        for (Dish dish : status) {
            Integer status1 = dish.getStatus();
                if (status1 == StatusConstant.ENABLE) {
                    throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
                }
        }
        //判断菜品是否关联了套餐
        List<Long> setMealId = setMealDishMapper.setMealId(id);
            if (setMealId != null && setMealId.size() != 0){
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
            //删除菜品数据
            /*for (Long aLong : id) {
                    dishMapper.delete(aLong);
                    //删除菜品关联的口味
                    dishFlavorMapper.deleteFlover(aLong);
            }*/
        dishMapper.deleteByIds(id);
        dishFlavorMapper.deleteFloverByIds(id);
        }
    }
