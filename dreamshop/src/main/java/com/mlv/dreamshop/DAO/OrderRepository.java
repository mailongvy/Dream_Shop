package com.mlv.dreamshop.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mlv.dreamshop.Model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
