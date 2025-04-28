package com.mlv.dreamshop.service.Cart;

import java.math.BigDecimal;

import com.mlv.dreamshop.Model.Cart;
import com.mlv.dreamshop.Model.User;
import org.springframework.transaction.annotation.Transactional;

public interface ICartService {
    // get cart by the id
    Cart getCart(Long id);

    // clear the cart by the id
    void clearCart(Long id); 

    //get total price for the cart by the id
    BigDecimal getTotalPrice(Long id);

    Long initializeNewCart();


    Cart initializeNewCart(User user);

    Cart getCartsByUserId(Long userId);


}
