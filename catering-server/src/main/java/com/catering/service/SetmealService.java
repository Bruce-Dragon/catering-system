package com.catering.service;

import com.catering.dto.SetmealDTO;
import com.catering.dto.SetmealPageQueryDTO;
import com.catering.entity.Setmeal;
import com.catering.entity.SetmealDish;
import com.catering.result.PageResult;
import com.catering.vo.SetmealVO;

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
