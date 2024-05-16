package com.catering.controller.user;

import com.catering.dto.ShoppingCartDTO;
import com.catering.entity.ShoppingCart;
import com.catering.result.Result;
import com.catering.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
       List<ShoppingCart> shoppingCart = shoppingCartService.list();
        return Result.success(shoppingCart);
    }
    @DeleteMapping("/clean")
    public Result clean(){
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }
    @PostMapping("/sub")
    public Result invert(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.invert(shoppingCartDTO);

        return Result.success();
    }
}
