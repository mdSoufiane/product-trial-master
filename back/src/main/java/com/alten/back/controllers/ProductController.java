package com.alten.back.controllers;

import com.alten.back.controllers.api.ProductApi;
import com.alten.back.dtos.ProductDto;
import com.alten.back.entities.Product;
import com.alten.back.enums.InventoryStatus;
import com.alten.back.services.ProductService;
import core.exceptions.ExceptionClass;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController implements ProductApi {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save")
    public Product createProduct(@RequestBody ProductDto productDto) throws ExceptionClass {
        return productService.savewithoutImage(productDto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponse(useReturnTypeSchema = true)
    public Product saveProduct(@RequestParam("file") MultipartFile file,
                              @RequestParam("code") String code,
                              @RequestParam("name") String name,
                              @RequestParam("description") String description,
                              @RequestParam("price") Double price,
                              @RequestParam("quantity") int quantity,
                              @RequestParam("internalReference") String internalReference,
                              @RequestParam("inventoryStatus") String inventoryStatus,
                              @RequestParam("rating") double rating,
                              @RequestParam("categoryId") Long categoryId ) throws ExceptionClass {
        ProductDto productDto = ProductDto.builder()
                .code(code)
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .internalReference(internalReference)
                .inventoryStatus(InventoryStatus.valueOf(inventoryStatus))
                .rating(rating)
                .categoryId(categoryId)
                .build();
        return productService.save(file, productDto);
    }

    @GetMapping()
    public List<Product> getAllProducts() throws ExceptionClass {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") Long id) throws ExceptionClass {
        return productService.getProductById(id);
    }

    @PatchMapping("/{id}")
    @ApiResponse(useReturnTypeSchema = true)
    public Product updateProduct(@PathVariable("id") Long id, @RequestBody ProductDto updatedProduct) throws ExceptionClass {
        return productService.updateProductById(id, updatedProduct);
    }
    @PatchMapping(path = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponse(useReturnTypeSchema = true)
    public Product updateProductImage(@PathVariable("id") Long id,
                                      @RequestParam("image") MultipartFile image) throws ExceptionClass {
        return productService.updateImageProductById(id, image);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) throws ExceptionClass {
        productService.deleteProductById(id);
    }

    @GetMapping(path = "/productImage/{productId}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long productId) throws IOException, ExceptionClass {
        Product product =  productService.getProductById(productId);

        String relativeFilePath = product.getImage();

        Path filePath = Paths.get(relativeFilePath).toAbsolutePath();

        String extension = relativeFilePath.substring(relativeFilePath.lastIndexOf(".") + 1).toLowerCase();
        MediaType mediaType = switch (extension.toLowerCase()){
            case "png" -> MediaType.IMAGE_PNG;
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "gif" -> MediaType.IMAGE_GIF;
            case "pdf" -> MediaType.APPLICATION_PDF;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };

        byte[] fileBytes = Files.readAllBytes(filePath);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(fileBytes);
    }
}