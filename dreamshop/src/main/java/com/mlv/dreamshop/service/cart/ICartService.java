package com.mlv.dreamshop.service.cart;

import java.math.BigDecimal;

import com.mlv.dreamshop.Model.Cart;

public interface ICartService {
    // get cart
    public Cart getCartById(Long id);

    // clear the cart by id
    public void clearCart(Long id);

    // get total price for the cart
    BigDecimal getTotalPrice(Long id);
}
