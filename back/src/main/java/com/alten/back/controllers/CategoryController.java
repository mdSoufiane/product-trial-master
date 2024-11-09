package com.alten.back.controllers;

import com.alten.back.controllers.api.CategoryApi;
import com.alten.back.entities.Category;
import com.alten.back.services.CategoryServices;
import core.exceptions.ExceptionClass;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController implements CategoryApi {
    private final CategoryServices categoryService;

    public CategoryController(CategoryServices categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping()
    public Category saveCategory(@RequestBody Category category){
        try {
            return categoryService.saveCategory(category);
        } catch (ExceptionClass e) {
            e.printStackTrace();
        }
        return category;
    }

    @PatchMapping()
    public Category editCategory(@RequestBody Category category){
        try {
            return categoryService.updateCategory(category);
        } catch (ExceptionClass e) {
            e.printStackTrace();
        }
        return category;
    }

    @GetMapping()
    public List<Category> getAllCategories(){
        try {
            return categoryService.getAllCategories();
        } catch (ExceptionClass e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable("id") Long id){
        try {
            return categoryService.getCategoryById(id);
        } catch (ExceptionClass e) {
            e.printStackTrace();
        }
        return null;
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategoryById(@PathVariable("categoryId") Long categoryId){
        try {
            categoryService.deleteCategory(categoryId);
        } catch (ExceptionClass e) {
            e.printStackTrace();
        }
    }
}
