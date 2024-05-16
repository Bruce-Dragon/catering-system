package com.catering.service;

import com.catering.dto.CategoryDTO;
import com.catering.dto.CategoryPageQueryDTO;
import com.catering.entity.Category;
import com.catering.result.PageResult;

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

     /**
      * 根据分类类型查询分类
      * @param type
      * @return
      */
    List<CategoryDTO> categoryByType(Integer type);
}
