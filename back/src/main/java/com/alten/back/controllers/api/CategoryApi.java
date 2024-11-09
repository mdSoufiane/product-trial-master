package com.alten.back.controllers.api;

import com.alten.back.entities.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categories")
public interface CategoryApi {

    @Operation(
            summary = "Create a category",
            description = "This method allows adding a new category.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category Created",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    implementation = Category.class
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid object"
                    )
            }
    )
    @PostMapping()
    Category saveCategory(@RequestBody Category category);

    @Operation(
            summary = "Update a category",
            description = "This method allows updating an existing category.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category Updated",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    implementation = Category.class
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid object"
                    )
            }
    )
    @PatchMapping()
    Category editCategory(@RequestBody Category category);

    @Operation(
            summary = "Get all categories",
            description = "This method allows retrieving a list of all categories.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of categories",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(
                                                    schema = @Schema(
                                                            implementation = Category.class
                                                    )
                                            )
                                    )
                            }
                    )
            }
    )
    @GetMapping()
    List<Category> getAllCategories();

    @Operation(
            summary = "Get category by ID",
            description = "This method allows retrieving a category by its ID.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "id",
                            description = "Category ID"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    implementation = Category.class
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found"
                    )
            }
    )
    @GetMapping("/{id}")
    Category getCategoryById(@PathVariable("id") Long id);

    @Operation(
            summary = "Delete category by ID",
            description = "This method allows deleting a category by its ID.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "categoryId",
                            description = "Category ID"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Category deleted"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found"
                    )
            }
    )
    @DeleteMapping("/{categoryId}")
    void deleteCategoryById(@PathVariable("categoryId") Long categoryId);
}

