package com.mlv.dreamshop.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mlv.dreamshop.DAO.CategoryRepository;
import com.mlv.dreamshop.Model.Category;
import com.mlv.dreamshop.exceptions.AlreadyExistsException;
import com.mlv.dreamshop.exceptions.CategoryNotFoundException;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    // define the fields
    private final CategoryRepository categoryRepository;


    @Override
    public Category addCategory(Category category) {
        // TODO Auto-generated method stub
        // hàm 1 là lọc xem category đã có hay ch
        // nếu ch có thì repo sẽ save lại category mới (add)
        // nếu đã có category thì sẽ thông báo là đã tồn tại
        return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
                                    .map(categoryRepository::save)
                                    .orElseThrow(() -> new AlreadyExistsException(category.getName() + " is already found"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        // TODO Auto-generated method stub
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, () -> {throw new CategoryNotFoundException("Category Not Found");});
        
    }

    @Override
    public List<Category> getAllCategories() {
        // TODO Auto-generated method stub
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        // TODO Auto-generated method stub
        return categoryRepository.findById(id)
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        // TODO Auto-generated method stub
        return categoryRepository.findByName(name);
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        // TODO Auto-generated method stub
        // các bước update
        // 1: tìm category theo id
        // 2: chuyển đổi category vừa tìm được thành tên mới r lưu
        // khi không tìm thấy category thì đưa về lõi ko tìm thấy category
        return categoryRepository.findById(id)
                        .map(oldCategory -> {
                            oldCategory.setName(category.getName());
                            return categoryRepository.save(oldCategory);
                        })
                        .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }
    
}
