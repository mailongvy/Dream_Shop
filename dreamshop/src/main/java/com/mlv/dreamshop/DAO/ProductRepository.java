package com.mlv.dreamshop.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mlv.dreamshop.Model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // find by category
    List<Product> findByCategoryName(String category);

    // find by brand
    List<Product> findByBrand(String brand);

    // handmade
    // // find by brand and category
    // @Query(value =  "select * from product p join category c on p.category_id = c.id where p.brand = :brand AND c.name = :category", nativeQuery = true)
    // List<Product> findByCategoryAndBrand(@Param("brand") String brand, @Param("category") String category);

    // find by brand and categoty
    List<Product> findByCategoryNameAndBrand(String category, String brand);

    // find by name
    List<Product> findByName(String name);

    // find by name and brand
    List<Product> findByBrandAndName(String brand, String name);

    // count products by brand and name
    Long countByBrandAndName(String brand, String name);

    boolean existsByNameAndBrand(String name, String brand);
}
