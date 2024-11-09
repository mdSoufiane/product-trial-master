package com.alten.back.services;

import com.alten.back.entities.Category;
import core.exceptions.ExceptionClass;

import java.util.List;

public interface CategoryServices {
    Category getCategoryByName(String categoryName) throws ExceptionClass;
    Category getCategoryById(Long categoryId) throws ExceptionClass;
    Category saveCategory(Category category) throws ExceptionClass;
    Category updateCategory(Category category) throws ExceptionClass;
    void deleteCategory(Long categoryId) throws ExceptionClass;
    List<Category> getAllCategories() throws ExceptionClass;
}
