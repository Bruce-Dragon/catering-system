package com.catering.controller.admin;

import com.catering.dto.SetmealDTO;
import com.catering.dto.SetmealPageQueryDTO;
import com.catering.result.PageResult;
import com.catering.result.Result;
import com.catering.service.SetmealService;
import com.catering.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @CacheEvict(cacheNames = "setmealCache" , allEntries = true)
    public Result insertSetmeal(@RequestBody SetmealDTO setmealDTO){
        setmealService.insertSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 更新套餐
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @CacheEvict(cacheNames = "setmealCache" , allEntries = true)
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO){
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<SetmealVO> invert(@PathVariable Long id){
        SetmealVO setmealVO = setmealService.invert(id);
        return Result.success(setmealVO);
    }

}
