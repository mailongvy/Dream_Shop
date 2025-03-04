package com.mlv.dreamshop.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mlv.dreamshop.Model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    // Cart findByUserId(Long userId);
}
