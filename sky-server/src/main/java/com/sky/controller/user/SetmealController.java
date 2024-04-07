package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    @Cacheable(cacheNames = "setmealCache" , key = "#categoryId")
    @GetMapping("/list")
    public Result<List<Setmeal>> setmealByCategoryId(Long categoryId){
        List<Setmeal> setmeal = setmealService.setmealByCategoryId(categoryId);
        return Result.success(setmeal);
    }
    /*@GetMapping("/list")
    @ApiOperation("根据分类id查询套餐")
    public Result<List<Setmeal>> list(Long categoryId) {
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        //setmeal.setStatus(StatusConstant.ENABLE);

        List<Setmeal> list = setmealService.list(setmeal);
        return Result.success(list);
    }*/

    @GetMapping("/dish/{id}")
    public Result<List<SetmealDish>> findSetmealDish(@PathVariable Long id){
        List<SetmealDish> setmealDish = setmealService.findSetmealWithDish(id);
        return Result.success(setmealDish);
    }
}
