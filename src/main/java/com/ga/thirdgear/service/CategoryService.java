package com.ga.thirdgear.service;

import com.ga.thirdgear.exception.InformationExistException;
import com.ga.thirdgear.exception.InformationNotFoundException;
import com.ga.thirdgear.model.Category;
import com.ga.thirdgear.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new InformationNotFoundException("Category not found with id: " + id));
    }

    public Category createCategory(Category category) {
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new InformationExistException("Category already exists with name: " + category.getName());
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category category) {
        Category existing = getCategoryById(id);
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        return categoryRepository.save(existing);
    }

    public void deleteCategory(Long id) {
        Category existing = getCategoryById(id);
        categoryRepository.delete(existing);
    }
}