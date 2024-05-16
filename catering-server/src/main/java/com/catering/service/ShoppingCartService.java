package com.catering.service;

import com.catering.dto.ShoppingCartDTO;
import com.catering.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {


    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> list();

    void cleanShoppingCart();

    void invert(ShoppingCartDTO shoppingCartDTO);
}
