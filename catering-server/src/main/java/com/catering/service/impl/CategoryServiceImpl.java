package com.catering.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.catering.constant.MessageConstant;
import com.catering.constant.StatusConstant;
import com.catering.context.BaseContext;
import com.catering.dto.CategoryDTO;
import com.catering.dto.CategoryPageQueryDTO;
import com.catering.entity.Category;
import com.catering.exception.DeletionNotAllowedException;
import com.catering.mapper.CategoryMapper;
import com.catering.mapper.DishMapper;
import com.catering.mapper.SetmealMapper;
import com.catering.result.PageResult;
import com.catering.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setMealMapper;

    /**
     * 分类 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 分类启用、禁用
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        log.info("更新:");
        Category build = Category.builder().
                status(status)
                .id(id)
                .updateUser(BaseContext.getCurrentId())
                .updateTime(LocalDateTime.now()).build();
        categoryMapper.update(build);
    }

    /**
     * 更新分类信息
     * @param categoryDTO
     */
    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.update(category);
    }

    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setUpdateUser(BaseContext.getCurrentId());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateTime(LocalDateTime.now());
        category.setStatus(StatusConstant.DISABLE);
        categoryMapper.save(category);

    }

    /**
     * 根据分类id删除分类
     * @param id
     */
    @Override
    public void delete(Integer id) {
         if (dishMapper.coutCategoryById(id) > 0){
             throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
         }
         setMealMapper.coutByCategoryId(id);
         if (setMealMapper.coutByCategoryId(id) > 0){
             throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
         }
         categoryMapper.delete(id);
    }

    /**
     * 根据分类id查询分类
     * @param type
     * @return
     */
    @Override
    public List<Category> list(Integer type) {
        List list = categoryMapper.list(type);
        return list;
    }
    /**
     * 根据分类类型查询分类
     * @param
     * @return
     */
    @Override
    public List<CategoryDTO> categoryByType(Integer type) {
        List<CategoryDTO> category = categoryMapper.categoryList(type);
        return category;
    }
}
