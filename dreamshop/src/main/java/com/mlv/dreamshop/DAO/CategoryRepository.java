package com.mlv.dreamshop.DAO;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mlv.dreamshop.Model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // find by name 
    Category findByName(String name);

    boolean existsByName(String name);
    
}
