package com.mlv.dreamshop.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mlv.dreamshop.Model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
