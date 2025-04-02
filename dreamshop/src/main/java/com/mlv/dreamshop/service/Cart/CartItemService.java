package com.mlv.dreamshop.service.Cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mlv.dreamshop.DAO.CartItemRepository;
import com.mlv.dreamshop.DAO.CartRepository;
import com.mlv.dreamshop.Model.Cart;
import com.mlv.dreamshop.Model.CartItem;
import com.mlv.dreamshop.Model.Product;
import com.mlv.dreamshop.exceptions.ResourceNotFound;
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
    @Transactional
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        // TODO Auto-generated method stub
        // 1. get the cart
        //2. get the product
        //3. check if the product already in the cart
        //4. if yes, then increase the quantity with requested quantity
        //5. if no, the initiate a new cartitem entry.
        Cart cart = cartService.getCart(cartId);

        Product product = productService.findById(productId);

        CartItem cartItem = cart.getItems().stream()
                                          .filter(item-> item.getProduct().getId().equals(productId))
                                          .findFirst()
                                          .orElse(new CartItem());
        // if the cart item is not found
        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }
        // if the cart item is found
        else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFormCart(Long cartId, Long productId) {
        // TODO Auto-generated method stub
        Cart cart = cartService.getCart(cartId);

        CartItem itemToRemove = getCartItem(cartId, productId);

        cart.removeItem(itemToRemove);

        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        // TODO Auto-generated method stub
        Cart cart = cartService.getCart(cartId);
        cart.getItems().stream()
                       .filter(item -> item.getProduct().getId().equals(productId))
                       .findFirst()
                       .ifPresent(item -> {
                            item.setQuantity(quantity);
                            item.setUnitPrice(item.getProduct().getPrice());
                            item.setTotalPrice();
                       });
        BigDecimal totalAmount = cart.getItems()
                                     .stream()
                                     .map(CartItem::getTotalPrice)
                                     .reduce(BigDecimal.ZERO, BigDecimal::add);
                                    

        cart.setTotalAmount(totalAmount);

        cartRepository.save(cart);
    }
    
    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems().stream()
                              .filter(item -> item.getProduct().getId().equals(productId))
                              .findFirst()
                              .orElseThrow(() -> new ResourceNotFound("Item not found"));
    }

}
