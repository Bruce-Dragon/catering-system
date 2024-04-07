package com.sky.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.callback.PrivateKeyCallback;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    @Transactional
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //先查询购物车中有没有该数据
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectShoppingCart(shoppingCart);
        //如果有在此基础上数量加一
        if (shoppingCartList != null && shoppingCartList.size() > 0){
            ShoppingCart shoppingCart1 = shoppingCartList.get(0);
            shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
            shoppingCartMapper.updateShoppingCart(shoppingCart1);
        }else {
            //如果没有执行insert插入数据
            if (shoppingCart.getDishId() != null) {
                Dish dish = dishMapper.selectDishById(shoppingCart.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                Setmeal setmeal = setmealMapper.invertSetmeal(shoppingCart.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);
            shoppingCartMapper.insertShoppingCart(shoppingCart);
        }


    }

    @Override
    public List<ShoppingCart> list() {
        Long currentId = BaseContext.getCurrentId();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(currentId);
        return shoppingCartList;
    }

    @Override
    public void cleanShoppingCart() {
        Long currentId = BaseContext.getCurrentId();
        shoppingCartMapper.delete(currentId);
    }

    @Override
    public void invert(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectShoppingCart(shoppingCart);
        ShoppingCart shoppingCart1 = shoppingCartList.get(0);
        if (shoppingCartList != null && shoppingCartList.size() > 0) {
            shoppingCart1.setNumber(shoppingCart1.getNumber() - 1);
            shoppingCartMapper.updateShoppingCart(shoppingCart1);
        }
        if (shoppingCart1.getNumber() == 0){
            Long id = shoppingCart1.getId();
            shoppingCartMapper.deleteShoppingCart(id);
        }
    }
}
