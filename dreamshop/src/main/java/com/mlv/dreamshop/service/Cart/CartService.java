package com.mlv.dreamshop.service.Cart;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import com.mlv.dreamshop.Model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mlv.dreamshop.DAO.CartItemRepository;
import com.mlv.dreamshop.DAO.CartRepository;
import com.mlv.dreamshop.Model.Cart;
import com.mlv.dreamshop.exceptions.ResourceNotFound;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public void clearCart(Long id) {
        // TODO Auto-generated method stub
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);

        
    }

    
    @Override
    public Cart getCart(Long id) {
        // TODO Auto-generated method stub
        Cart cart = cartRepository.findById(id).orElseThrow(() ->new ResourceNotFound("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        // TODO Auto-generated method stub
        //bước 1: lấy cart càn tính tổng tiền
        Cart cart = getCart(id);


        return cart.getTotalAmount();
    }

    @Override
    @Transactional
    public Long initializeNewCart() {
        Cart cart = new Cart();
        cart.setTotalAmount(BigDecimal.ZERO);
        cart.setItems(new HashSet<>());
        cartRepository.save(cart);
        return cart.getId();
    }

    @Transactional
    @Override
    // get cart by user id
    public Cart initializeNewCart(User user) {
        return Optional.ofNullable(getCartsByUserId(user.getId()))
                        .orElseGet(() -> {
                            Cart cart = new Cart();
                            cart.setUser(user);
                            cart.setTotalAmount(BigDecimal.ZERO);
                            cart.setItems(new HashSet<>());
                            cartRepository.save(cart);
                            return cart;
                        });
    }


    @Override
    public Cart getCartsByUserId(Long userId) {
        // TODO Auto-generated method stub
        return cartRepository.findByUserId(userId);
    }

    
    
}
