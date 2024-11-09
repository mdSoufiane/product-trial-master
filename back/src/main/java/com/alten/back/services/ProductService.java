package com.alten.back.services;

import com.alten.back.dtos.ProductDto;
import com.alten.back.entities.Product;
import core.exceptions.ExceptionClass;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Product save(MultipartFile image, ProductDto productDto) throws ExceptionClass;
    Product savewithoutImage(ProductDto productDto) throws ExceptionClass;
    Product getProductById(Long id) throws ExceptionClass;
    Product getProductByName(String name) throws ExceptionClass;
    List<Product> getAllProducts() throws ExceptionClass;
    void deleteProductById(Long id) throws ExceptionClass;
    Product updateProductById(Long productId, ProductDto updatedProduct) throws ExceptionClass;
    Product updateImageProductById(Long productId, MultipartFile image) throws ExceptionClass;
    byte[] getProductImageById(Long id) throws ExceptionClass;
}
