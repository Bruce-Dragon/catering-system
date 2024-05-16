package com.catering.controller.admin;

import com.catering.dto.DishDTO;
import com.catering.dto.DishPageQueryDTO;
import com.catering.result.PageResult;
import com.catering.result.Result;
import com.catering.service.DishService;
import com.catering.vo.DishVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api("菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){

        PageResult page = dishService.page(dishPageQueryDTO);

        return Result.success(page);

    }

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品:{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 根据菜品id批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        dishService.deleteById(ids);
        return Result.success();
    }

    /**
     * 根据菜品id查询菜品数据，用于回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishVO> selectDish(@PathVariable Long id){
       DishVO dish =  dishService.selectDishById(id);
        return Result.success(dish);
    }

    /**
     * 更新菜品数据
     * @param dishDTO
     * @return
     */
    @PutMapping
    public Result updateWithFlavor(@RequestBody DishDTO dishDTO){

        dishService.updateDish(dishDTO);

        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<DishVO>> dishCategory(Long categoryId){
        List<DishVO> list = dishService.dishByCategoryId(categoryId);
        return Result.success(list);
    }
}
