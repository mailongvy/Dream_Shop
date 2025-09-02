package com.mlv.dreamshop.service.Cart;

import com.mlv.dreamshop.Model.CartItem;

public interface ICartItemService {
    // add item to the cart
    void addItemToCart(Long cartId, Long productId, int quantity);

    // remove the item from the cart
    void removeItemFormCart(Long cartId, Long productId);

    // update quantity
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);

    // New methods for cart item operations by itemId
    void updateItemQuantityByItemId(Long itemId, int quantity);
    void removeItemByItemId(Long itemId);
}
