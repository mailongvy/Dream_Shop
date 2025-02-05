package com.mlv.dreamshop.service.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mlv.dreamshop.DAO.ProductRepository;
import com.mlv.dreamshop.Model.Product;
import com.mlv.dreamshop.exceptions.ProductNotFoundException;

@Service
public class ProductService implements IProductService {
    // define the productrepo
    private ProductRepository productRepository;

    
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void deleteProductById(Long id) {
        // TODO Auto-generated method stub
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {throw new ProductNotFoundException("Product not found");});

        
    }

    @Override
    public Product findById(Long id) {
        // TODO Auto-generated method stub
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        // TODO Auto-generated method stub
        List<Product> list = productRepository.findAll();
        return list;
    }

    @Override
    public Product save(Product product) {
        // TODO Auto-generated method stub
        return productRepository.save(product);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        // TODO Auto-generated method stub
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String brand, String category) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long countProductsByBrandAndName(String name, String brand) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Product> getProductsByBrandAndName(String name, String brand) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Product> getProductsByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    

    

    
    
}
