package com.mlv.dreamshop.service.Cart;

import com.mlv.dreamshop.Model.CartItem;

public interface ICartItemService {
    // add item to the cart
    void addItemToCart(Long cartId, Long productId, int quantity);

    // remove the item from the cart
    


}
