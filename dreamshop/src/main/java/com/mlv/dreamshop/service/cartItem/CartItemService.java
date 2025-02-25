package com.mlv.dreamshop.service.cartItem;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.mlv.dreamshop.DAO.CartItemRepository;
import com.mlv.dreamshop.DAO.CartRepository;
import com.mlv.dreamshop.Model.Cart;
import com.mlv.dreamshop.Model.CartItem;
import com.mlv.dreamshop.Model.Product;
import com.mlv.dreamshop.exceptions.ResourceNotFound;
import com.mlv.dreamshop.service.cart.ICartService;
import com.mlv.dreamshop.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;

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

        // get the cart to add cart item
        Cart cart = cartService.getCartById(cartId);

        // get the product to add to the cart
        Product product = productService.findById(productId);

        // check the product is in the cart or not
        CartItem cartItem = cart.getCartItems()
                                .stream()
                                .filter((item) -> item.getProduct().getId().equals(productId))
                                .findFirst()
                                .orElse(new CartItem());

        // if not, create the new cart item and add to the cart
        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }
        // if yes, increase the quantity for the cartitem
        else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        cartItem.setTotalPrice();

        cart.addItem(cartItem);


        cartItemRepository.save(cartItem);
        cartRepository.save(cart);



        
    }

    @Override
    public void removeItemFromTheCart(Long cartId, Long productId) {
        // TODO Auto-generated method stub
        Cart cart = cartService.getCartById(productId);

        CartItem cartItem = getCartItem(cartId, productId);

        cart.removeItem(cartItem);
        cartRepository.save(cart);
    }



    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        // TODO Auto-generated method stub
        Cart cart = cartService.getCartById(cartId);

        cart.getCartItems()
            .stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .ifPresent(item -> {
                item.setQuantity(quantity);
                item.setUnitPrice(item.getProduct().getPrice());
                item.setTotalPrice(); // cập nhật lại giá
            });
        
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
        

    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCartById(productId);
        return cart.getCartItems()
                   .stream()
                   .filter(item -> item.getProduct().getId().equals(productId))
                   .findFirst().orElseThrow(() -> new ResourceNotFound("Product Not found"));
    }
    
}
