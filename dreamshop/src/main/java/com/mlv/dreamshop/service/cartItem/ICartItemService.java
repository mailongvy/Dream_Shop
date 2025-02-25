package com.mlv.dreamshop.service.cartItem;

import com.mlv.dreamshop.Model.CartItem;

public interface ICartItemService {
    // add cart item for the cart
    void addItemToCart(Long cartId, Long productId, int quantity);

    // void remove item from the cart
    void removeItemFromTheCart(Long cartId, Long productId);

    // update quantity fot item
    void updateItemQuantity(Long cartId, Long productId, int quantity);
    
    // get carrt item
    CartItem getCartItem(Long cartId, Long productId);
}
