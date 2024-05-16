package com.catering.controller.admin;

import com.catering.dto.CategoryDTO;
import com.catering.dto.CategoryPageQueryDTO;
import com.catering.entity.Category;
import com.catering.result.PageResult;
import com.catering.result.Result;
import com.catering.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin/category")
@Slf4j
@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 分类 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询:{}",categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 分类启用/禁用
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("更新:");
        categoryService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * 更新分类信息
     * @param categoryDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return Result.success();
    }
    @PostMapping
    public Result save(@RequestBody CategoryDTO categoryDTO){
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 根据分类id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public Result delete(Integer id){
        categoryService.delete(id);
        return Result.success();
    }
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type){
        log.info("进来了");
        List list = categoryService.list(type);
        return Result.success(list);
    }
}
