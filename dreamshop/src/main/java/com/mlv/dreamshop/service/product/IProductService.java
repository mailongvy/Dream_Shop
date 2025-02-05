package com.mlv.dreamshop.service.product;

import java.util.List;

import com.mlv.dreamshop.Model.Product;

public interface IProductService {
    // add product and update
    Product save(Product product);

    // find the products by id
    Product findById(Long id);

    // delete product by id
    void deleteProductById(Long id);

    // find all the products
    List<Product> getAllProducts();

    // get product by category id
    List<Product> getProductsByCategory(String category);

    // get product by brand
    List<Product> getProductsByBrand(String brand);
    
    //get product by category and brand
    List<Product> getProductsByCategoryAndBrand(String brand, String category);

    // get products by name
    List<Product> getProductsByName(String name);

    // get products by branch and name
    List<Product> getProductsByBrandAndName(String name, String brand);

    // count products by name and brand
    Long countProductsByBrandAndName(String name, String brand);
}
