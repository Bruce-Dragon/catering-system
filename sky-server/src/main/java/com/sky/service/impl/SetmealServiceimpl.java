package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceimpl implements SetmealService {

    @Autowired
    private SetmealMapper setMealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;
    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    @Override
    public List<Setmeal> setmealByCategoryId(Long categoryId) {
       List<Setmeal> setmeal = setMealMapper.setmeal(categoryId);
        return setmeal;
    }
    /**
     * 条件查询
     * @param setmeal
     * @return
     */
  /*  public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setMealMapper.list(setmeal);
        return list;
    }*/

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = setMealMapper.page(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void insertSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setMealMapper.insert(setmeal);
        Long id = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(id);
        }
        setMealDishMapper.insert(setmealDishes);
    }

    @Override
    public void updateSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setMealMapper.update(setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && setmealDishes.size() > 0){
            Long id = setmealDTO.getId();
            setMealDishMapper.delete(id);
            setMealDishMapper.insert(setmealDishes);
        }


    }

    @Override
    public SetmealVO invert(Long id) {
       Setmeal setmeal = setMealMapper.invertSetmeal(id);
       SetmealVO setmealVO = new SetmealVO();
       BeanUtils.copyProperties(setmeal,setmealVO);
       List<SetmealDish> setmealDishes = setMealDishMapper.invertSetmealDish(id);
       setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    @Override
    public List<SetmealDish> findSetmealWithDish(Long id) {
        List<SetmealDish> setmealDish = setMealDishMapper.findDish(id);
        return setmealDish;
    }
}
