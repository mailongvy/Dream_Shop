package com.mlv.dreamshop.service.cartItem;

import org.springframework.stereotype.Service;

import com.mlv.dreamshop.DAO.CartItemRepository;
import com.mlv.dreamshop.Model.Cart;
import com.mlv.dreamshop.Model.Product;
import com.mlv.dreamshop.service.cart.ICartService;
import com.mlv.dreamshop.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        // TODO Auto-generated method stub
        /*
         * 1. get the cart
         * 2. get the product
         * 3. check if the product in the cart
         * 4. if yes, then increase the quantity with the requested quantity
         * 5. ì no, the initiate a new CartItem entry
         */

        Cart cart = cartService.getCartById(cartId);
        Product product = productService.findById(productId);

        
    }

    @Override
    public void removeItemFromTheCart(Long cartId, Long productId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        // TODO Auto-generated method stub
        
    }
    
}
