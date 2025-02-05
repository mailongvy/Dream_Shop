package com.mlv.dreamshop.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mlv.dreamshop.Model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // find by category
    List<Product> findByCategory(String category);
}
