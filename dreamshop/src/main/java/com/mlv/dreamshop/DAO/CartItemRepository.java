package com.mlv.dreamshop.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mlv.dreamshop.Model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    void deleteAllByCartId(Long id);

}
