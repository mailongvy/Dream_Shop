package com.mlv.dreamshop.service.Cart;

import java.math.BigDecimal;

import com.mlv.dreamshop.Model.Cart;

public interface ICartService {
    // get cart by the id
    Cart getCart(Long id);

    // clear the cart by the id
    void clearCart(Long id); 

    //get total price for the cart by the id
    BigDecimal getTotalPrice(Long id);

    Long initializeNewCart();


}
