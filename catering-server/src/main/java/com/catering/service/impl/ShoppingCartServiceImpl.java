package com.catering.service.impl;

import com.catering.context.BaseContext;
import com.catering.dto.ShoppingCartDTO;
import com.catering.entity.Dish;
import com.catering.entity.Setmeal;
import com.catering.entity.ShoppingCart;
import com.catering.mapper.DishMapper;
import com.catering.mapper.SetmealMapper;
import com.catering.mapper.ShoppingCartMapper;
import com.catering.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
