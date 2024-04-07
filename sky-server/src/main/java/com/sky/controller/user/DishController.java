package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("user/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<DishVO>> dishByCategoryId(Long categoryId){
        String key = "Dish_" + categoryId;
        //判断redis中是否有缓存数据，如果有直接返回给前端
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if(list != null && list.size() > 0){
            return Result.success(list);
      }
        //如果redis没有缓存数据，直接查数据库，后放入redis
        List<DishVO> dishVO = dishService.dishByCategoryId(categoryId);
        redisTemplate.opsForValue().set(key,dishVO);
        return Result.success(dishVO);
    }

}
