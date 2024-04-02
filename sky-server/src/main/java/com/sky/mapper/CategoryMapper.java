package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryMapper {
    /**
     * 分类 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    public Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 更新分类信息、状态
     * @param category
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    /**
     * 新增分类
     * @param category
     */
    @AutoFill(value = OperationType.INSERT)
    void save(Category category);

    /**
     * 根据分类id删除分类
     * @param id
     */
    void delete(Integer id);
    @Select("select * from sky_take_out.category where type=#{type}")
    List<Category> list(Integer type);
    /**
     * 根据分类类型查询分类
     * @param
     * @return
     */

    List<CategoryDTO> categoryList(Integer type);
}
