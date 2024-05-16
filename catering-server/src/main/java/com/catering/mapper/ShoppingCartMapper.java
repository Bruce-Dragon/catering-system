package com.catering.mapper;

import com.catering.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ShoppingCartMapper {


    List<ShoppingCart> selectShoppingCart(ShoppingCart shoppingCart);
    @Update("update sky_take_out.shopping_cart set number = #{number} where id = #{id}")
    void updateShoppingCart(ShoppingCart shoppingCart);

    @Insert("insert into sky_take_out.shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) VALUES (#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime})")
    void insertShoppingCart(ShoppingCart shoppingCart);
    @Select("select * from sky_take_out.shopping_cart where user_id = #{currenId};")
    List<ShoppingCart> list(Long currentId);

    @Delete("delete from sky_take_out.shopping_cart where user_id = #{currenId}")
    void delete(Long currentId);

    @Delete("delete from sky_take_out.shopping_cart where id = #{id}")
    void deleteShoppingCart(Long id);
}
