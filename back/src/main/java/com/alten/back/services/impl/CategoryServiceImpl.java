package com.alten.back.services.impl;

import com.alten.back.entities.Category;
import com.alten.back.repositories.CategoryRepository;
import com.alten.back.services.CategoryServices;
import core.exceptions.ExceptionClass;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryServices {

    private final CategoryRepository categoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retrieves a category by its name.
     *
     * <p>This method fetches a category from the repository using the provided category name.
     * If the category is found, it is returned; otherwise, an exception is thrown.
     *
     * @param categoryName the name of the category to be retrieved
     * @return the {@link Category} object associated with the given name
     * @throws ExceptionClass if an error occurs while retrieving the category or if the category
     *                        is not found
     */
    @Override
    public Category getCategoryByName(String categoryName) throws ExceptionClass {
        try {
            return categoryRepository.findByName(categoryName);
        } catch (Exception e) {
            throw new ExceptionClass("Error while getting the category with name: " + categoryName + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a category by its ID.
     *
     * <p>This method fetches a category from the repository using the provided category ID.
     * If the category is found, it is returned; otherwise, an exception is thrown.
     *
     * @param categoryId the ID of the category to be retrieved
     * @return the {@link Category} object associated with the given ID
     * @throws ExceptionClass if an error occurs while retrieving the category or if the category
     *                        with the given ID is not found
     */
    @Override
    public Category getCategoryById(Long categoryId) throws ExceptionClass {
        try {
            return categoryRepository.findById(categoryId).get();
        }catch (Exception e) {
            throw new ExceptionClass("Error while getting the category with id: " + categoryId + e.getMessage(), e);
        }
    }

    /**
     * Saves a new category to the repository.
     *
     * <p>This method persists a category in the repository. If the save operation is successful, the saved category is returned.
     * If an error occurs during the process, an exception is thrown.
     *
     * @param category the {@link Category} object to be saved
     * @return the saved {@link Category} object
     * @throws ExceptionClass if an error occurs while saving the category
     */
    @Override
    public Category saveCategory(Category category) throws ExceptionClass {
        try {
            return categoryRepository.save(category);
        }catch (Exception e) {
            throw new ExceptionClass("Error while saving the category with name: " + category.getName() + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing category in the repository.
     *
     * <p>This method updates the fields of an existing category based on the provided category object.
     * If the category is not found in the repository, an exception is thrown.
     * If the update operation is successful, the updated category is returned.
     *
     * @param category the {@link Category} object containing the updated information
     * @return the updated {@link Category} object
     * @throws ExceptionClass if an error occurs while updating the category or if the category is not found
     */
    @Override
    public Category updateCategory(Category category) throws ExceptionClass {
        try {
            Category existingCategory = categoryRepository.findById(category.getId())
                    .orElseThrow(() -> new ExceptionClass("Category not found with ID: " + category.getId()));

            existingCategory.setName(category.getName() != null ? category.getName() : existingCategory.getName());
            existingCategory.setDescription(category.getDescription() != null ? category.getDescription() : existingCategory.getDescription());

            return categoryRepository.save(existingCategory);
        } catch (Exception e) {
            throw new ExceptionClass("Error while updating the category: " + category.getName() + e.getMessage(), e);
        }
    }

    /**
     * Deletes a category from the repository by its ID.
     *
     * <p>This method first retrieves the category by its ID. If the category exists, it is deleted from the repository.
     * If any error occurs during the deletion process, an exception is thrown.
     *
     * @param categoryId the ID of the {@link Category} to be deleted
     * @throws ExceptionClass if an error occurs while retrieving or deleting the category
     */
    @Override
    public void deleteCategory(Long categoryId) throws ExceptionClass {
        try {
            Category category = this.getCategoryById(categoryId);
            if (category != null) {
                categoryRepository.delete(category);
            }
        } catch (Exception e) {
            throw new ExceptionClass("Error while deleting the category with ID: " + categoryId + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all categories from the repository.
     *
     * <p>This method fetches and returns a list of all categories stored in the repository.
     * If any error occurs during the retrieval process, an exception is thrown.
     *
     * @return a {@link List} of {@link Category} objects representing all categories
     * @throws ExceptionClass if an error occurs while retrieving the categories
     */
    @Override
    public List<Category> getAllCategories() throws ExceptionClass {
        try {
            return categoryRepository.findAll();
        } catch (Exception e) {
            throw new ExceptionClass("Error while getting the all the categories" + e.getMessage(), e);
        }
    }
}
