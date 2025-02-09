package com.mlv.dreamshop.service.category;

import java.util.List;

import com.mlv.dreamshop.Model.Category;

public interface ICategoryService {
    // get category by id
    Category getCategoryById(Long id);

    // get category by name
    Category getCategoryByName(String name);

    // get all category
    List<Category> getAllCategories();

    // add category
    Category addCategory(Category category);

    // update category
    Category updateCategory(Category category, Long id);

    // delete category
    void deleteCategoryById(Long id);

}
