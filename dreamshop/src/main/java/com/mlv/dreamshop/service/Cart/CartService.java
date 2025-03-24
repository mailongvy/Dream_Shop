package com.mlv.dreamshop.service.Cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

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
    
}
