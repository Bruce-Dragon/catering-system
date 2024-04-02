package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SetmealService {

  /**
   * 根据分类id查询套餐
   * @param //ategoryId
   * @return
   */
  public List<Setmeal> setmealByCategoryId(Long categoryId);
  /*public List<Setmeal> list(Setmeal setmeal);*/
  /**
   * 套餐分页查询
   * @param setmealPageQueryDTO
   * @return
   */
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    void insertSetmeal(SetmealDTO setmealDTO);

    void updateSetmeal(SetmealDTO setmealDTO);

    SetmealVO invert(Long id);

  List<SetmealDish> findSetmealWithDish(Long id);
}
