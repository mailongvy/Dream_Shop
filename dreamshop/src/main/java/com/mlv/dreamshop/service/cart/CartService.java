package com.mlv.dreamshop.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.mlv.dreamshop.DAO.CartItemRepository;
import com.mlv.dreamshop.DAO.CartRepository;
import com.mlv.dreamshop.Model.Cart;
import com.mlv.dreamshop.exceptions.CartException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class CartService implements ICartService {
    public final CartRepository cartRepository;
    public final CartItemRepository cartItemRepository;

    @Override
    public void clearCart(Long id) {
        // TODO Auto-generated method stub
        Cart cart = getCartById(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepository.deleteById(id);
        
    }

    @Override
    public Cart getCartById(Long id) {
        // TODO Auto-generated method stub
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new CartException("Cart Not Found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        // TODO Auto-generated method stub
        Cart cart = getCartById(id);
        return cart.getTotalAmount();
    }

    
    
}
