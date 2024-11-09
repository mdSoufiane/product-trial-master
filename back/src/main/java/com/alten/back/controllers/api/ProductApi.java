package com.alten.back.controllers.api;

import com.alten.back.dtos.ProductDto;
import com.alten.back.entities.Product;
import core.exceptions.ExceptionClass;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Products")
public interface ProductApi {

    @Operation(
            summary = "Create product without image",
            description = "This method allows adding a product without an image.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product Created",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    implementation = Product.class
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid object",
                            content = {
                                    @Content(
                                            array = @ArraySchema(
                                                    schema = @Schema(
                                                            implementation = String.class
                                                    )
                                            )
                                    )
                            }
                    )
            }
    )
    @PostMapping("/save")
    Product createProduct(@RequestBody ProductDto productDto) throws ExceptionClass;

    @Operation(
            summary = "Create product with image",
            description = "This method allows adding a product with an image.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product Created",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    implementation = Product.class
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid object",
                            content = {
                                    @Content(
                                            array = @ArraySchema(
                                                    schema = @Schema(
                                                            implementation = String.class
                                                    )
                                            )
                                    )
                            }
                    )
            }
    )
    @PostMapping()
    Product saveProduct(
            @RequestParam("file") MultipartFile file,
            @RequestParam("code") String code,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("quantity") int quantity,
            @RequestParam("internalReference") String internalReference,
            @RequestParam("inventoryStatus") String inventoryStatus,
            @RequestParam("rating") double rating,
            @RequestParam("categoryId") Long categoryId
    ) throws ExceptionClass;

    @Operation(
            summary = "Get all products",
            description = "This method allows retrieving the list of all products.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of products",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(
                                                    schema = @Schema(
                                                            implementation = Product.class
                                                    )
                                            )
                                    )
                            }
                    )
            }
    )
    @GetMapping()
    List<Product> getAllProducts() throws ExceptionClass;

    @Operation(
            summary = "Get product by ID",
            description = "This method allows retrieving a product by its ID.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "id",
                            description = "Product ID"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    implementation = Product.class
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"
                    )
            }
    )
    @GetMapping("/{id}")
    Product getProduct(@PathVariable("id") Long id) throws ExceptionClass;

    @Operation(
            summary = "Update product by ID",
            description = "This method allows updating a product by its ID.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "id",
                            description = "Product ID"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product updated",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    implementation = Product.class
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
    @PatchMapping("/{id}")
    Product updateProduct(
            @PathVariable("id") Long id,
            @RequestBody ProductDto updatedProduct
    ) throws ExceptionClass;

    @Operation(
            summary = "Update product image by ID",
            description = "This method allows updating the image of a product by its ID.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "id",
                            description = "Product ID"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product image updated",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    implementation = Product.class
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid image"
                    )
            }
    )
    @PatchMapping(path = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Product updateProductImage(
            @PathVariable("id") Long id,
            @RequestParam("image") MultipartFile image
    ) throws ExceptionClass;

    @Operation(
            summary = "Delete product by ID",
            description = "This method allows deleting a product by its ID.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "id",
                            description = "Product ID"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Product deleted"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"
                    )
            }
    )
    @DeleteMapping("/{id}")
    void deleteProduct(@PathVariable("id") Long id) throws ExceptionClass;

    @Operation(
            summary = "Get product image by ID",
            description = "This method allows retrieving a product image by its ID.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "productId",
                            description = "Product ID"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product image retrieved",
                            content = {
                                    @Content(
                                            mediaType = "image/*",
                                            schema = @Schema(type = "string", format = "binary")
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"
                    )
            }
    )
    @GetMapping(path = "/productImage/{productId}")
    ResponseEntity<byte[]> getProductImage(@PathVariable("productId") Long productId) throws IOException, ExceptionClass;
}

