package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {

     /**
      * 分类 分页查询
      * @param categoryPageQueryDTO
      * @return
      */
     PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

     /**
      * 分类启用/禁用
      * @param status
      * @param id
      */
     void startOrStop(Integer status, Long id);

     /**
      * 更新分类信息
      * @param categoryDTO
      */
     void update(CategoryDTO categoryDTO);

     /**
      * 新增分类
      * @param categoryDTO
      */
     void save(CategoryDTO categoryDTO);

     /**
      * 根据分类id删除分类
      * @param id
      */
     void delete(Integer id);

    List<Category> list(Integer type);
}
