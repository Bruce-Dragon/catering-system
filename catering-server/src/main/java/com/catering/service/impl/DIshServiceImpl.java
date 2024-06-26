package com.catering.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.catering.constant.MessageConstant;
import com.catering.constant.StatusConstant;
import com.catering.dto.DishDTO;
import com.catering.dto.DishPageQueryDTO;
import com.catering.entity.Dish;
import com.catering.entity.DishFlavor;
import com.catering.exception.DeletionNotAllowedException;
import com.catering.mapper.DishFlavorMapper;
import com.catering.mapper.DishMapper;
import com.catering.mapper.SetMealDishMapper;
import com.catering.result.PageResult;
import com.catering.service.DishService;
import com.catering.vo.DishVO;
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
        //批量删除
        dishMapper.deleteByIds(id);
        dishFlavorMapper.deleteFloverByIds(id);
        }
    /**
     * 根据菜品id查询菜品数据，用于回显
     * @param id
     * @return
     */

    @Override
    public DishVO selectDishById(Long id) {
        //根据id查询菜品数据
        Dish dish = dishMapper.selectDishById(id);
        Long id1 = dish.getId();
        //根据菜品id查询口味数据
        List<DishFlavor> dishFlavor = dishFlavorMapper.selectDishFlavorById(id1);
        //拷贝菜品和口味数据到DishVO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavor);
        return dishVO;
    }

    /**
     * 更新菜品数据
     * @param dishDTO
     */
    @Override
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //更新菜品数据
        dishMapper.updateDish(dish);
        //删除口味数据
        dishFlavorMapper.deleteFlover(dishDTO.getId());
        List<DishFlavor> flavors = dishDTO.getFlavors();
        //批量插入口味数据
        if (flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor ->
                    dishFlavor.setDishId(dishDTO.getId()));
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 根据分类id查询菜品及其口味数据
     * @param categoryId
     * @return
     */
    @Override
    public List<DishVO> dishByCategoryId(Long categoryId) {
       List<DishVO> list = dishMapper.categoryId(categoryId);
        for (DishVO dishVO : list) {
            Long id = dishVO.getId();
            List<DishFlavor> dishFlavor = dishFlavorMapper.flavorByCategoryId(id);
            dishVO.setFlavors(dishFlavor);
        }
        return list;
    }
}
