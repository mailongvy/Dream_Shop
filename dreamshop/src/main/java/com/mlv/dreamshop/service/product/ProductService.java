package com.mlv.dreamshop.service.product;

import java.util.List;
import java.util.Optional;
import com.mlv.dreamshop.Model.Category;

import org.springframework.stereotype.Service;

import com.mlv.dreamshop.DAO.ProductRepository;
import com.mlv.dreamshop.Model.Product;
import com.mlv.dreamshop.exceptions.ProductNotFoundException;
import com.mlv.dreamshop.request.AddProductRequest;

// import lombok.RequiredArgsConstructor;

@Service
// @RequiredArgsConstructor // những field được đánh dấu là final và @nonnull sẽ được tự động thêm vào constructor
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
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        // TODO Auto-generated method stub
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String brand, String category) {
        // TODO Auto-generated method stub
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public Long countProductsByBrandAndName(String name, String brand) {
        // TODO Auto-generated method stub
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String name, String brand) {
        // TODO Auto-generated method stub
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        // TODO Auto-generated method stub
        return productRepository.findByName(name);
    }

    @Override
    public Product addProuct(AddProductRequest request) {
        // TODO Auto-generated method stub

        return null;
        
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
            request.getName(), 
            request.getBrand(), 
            request.getPrice(),
            request.getInventory(),
            request.getDescription(), 
            category);
    }

    
    

    

    
    
}
