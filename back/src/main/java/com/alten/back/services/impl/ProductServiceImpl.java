package com.alten.back.services.impl;

import com.alten.back.dtos.ProductDto;
import com.alten.back.dtos.mapper.ProductDtoMapper;
import com.alten.back.entities.Product;
import com.alten.back.repositories.CategoryRepository;
import com.alten.back.repositories.ProductRepository;
import com.alten.back.services.ProductService;
import core.exceptions.ExceptionClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    /**
     * Directory path for file storage, configurable via the application properties.
     */
    @Value("${file.storage.directory}")
    private String storageDirectory;

    /**
     * Repository for handling CRUD operations related to Product entities.
     */
    private final ProductRepository productRepository;

    /**
     * Repository for handling CRUD operations related to Category entities.
     */
    private final CategoryRepository categoryRepository;

    /**
     * Constructs a new instance of ProductServiceImpl, initializing the required
     * repositories for handling product and category data.
     *
     * @param productRepository   repository for performing operations on Product entities
     * @param categoryRepository  repository for performing operations on Category entities
     */
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Saves a new product along with an uploaded image.
     *
     * <p>This method takes a `MultipartFile` representing the product image and a `ProductDto` containing product details.
     * It saves the image to the specified location and sets the file path in the `Product` entity.
     * The product is then mapped from the DTO, assigned a category, and saved in the database.
     * After saving, it retrieves the product by ID to ensure it was saved successfully.
     * </p>
     *
     * @param file : the image file associated with the product
     * @param productDto : the data transfer object containing product details
     * @return the saved `Product` entity
     * @throws ExceptionClass if any error occurs during image saving, product mapping, category retrieval,or product saving
     *
     */
    @Override
    public Product save(MultipartFile file, ProductDto productDto) throws ExceptionClass {
        try {
            String filePath = saveImage(file);

            Product product = ProductDtoMapper.toProduct(productDto);
            product.setImage(filePath);
            product.setCategory(categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new ExceptionClass("Category not found")));
            Product savedProduct = productRepository.save(product);
            return productRepository.findById(savedProduct.getId()).orElseThrow(() -> new ExceptionClass("Error while saving the product"));
        } catch (Exception e) {
            throw new ExceptionClass("Error while saving the product: " + e.getMessage(), e);
        }
    }

    /**
     * Saves a new product without an associated image.
     *
     * <p>This method takes a `ProductDto` containing product details, maps it to a `Product` entity, assigns the specified category to the product, and saves it in the database.
     * After saving, it retrieves the product by ID to confirm the save operation.
     * </p>
     *
     * @param productDto : the data transfer object containing product details
     * @return the saved `Product` entity
     * @throws ExceptionClass if the specified category is not found or if an error occurs during the product saving process
     *
     */
    @Override
    public Product savewithoutImage(ProductDto productDto)  throws ExceptionClass{
        try {
            Product product = ProductDtoMapper.toProduct(productDto);

            product.setCategory(categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new ExceptionClass("Category not found")));
            Product savedProduct = productRepository.save(product);
            return productRepository.findById(savedProduct.getId()).orElseThrow(() -> new ExceptionClass("Error while saving the product"));
        } catch (Exception e) {
            throw new ExceptionClass("Error while saving the product: " + e.getMessage(), e);
        }

    }

    /**
     * Retrieves a product by its ID.
     *
     * <p>This method searches the database for a product with the specified ID.
     * If the product is found, it is returned; otherwise, an `ExceptionClass` is thrown with an appropriate error message.
     * </p>
     *
     * @param id the unique identifier of the product to retrieve
     * @return the `Product` entity corresponding to the specified ID
     * @throws ExceptionClass if no product is found with the given ID or if an error occurs during the retrieval process
     *
     */
    @Override
    public Product getProductById(Long id) throws ExceptionClass {
        try {
            return productRepository.findById(id).orElseThrow(() -> new ExceptionClass("Product not found with ID: " + id));
        } catch (Exception e) {
            throw new ExceptionClass("Error while getting the product with ID: " + id, e);
        }
    }

    /**
     * Retrieves a product by its name.
     *
     * <p>This method searches the database for a product with the specified name. If the product is found, it is returned.
     * If an error occurs during the retrieval process, an `ExceptionClass is thrown with a relevant error message.
     * </p>
     *
     * @param name : the name of the product to retrieve
     * @return the `Product` entity corresponding to the specified name
     * @throws ExceptionClass : if an error occurs during the retrieval process
     */
    @Override
    public Product getProductByName(String name) throws ExceptionClass {
        try {
            return productRepository.findByName(name);
        } catch (Exception e) {
            throw new ExceptionClass("Error while getting the product with name: " + name, e);
        }
    }

    /**
     * Retrieves a list of all products.
     *
     *  <p>This method fetches all products from the database.
     *  If an error occurs during the retrieval process, an `ExceptionClass` is thrown with a relevant error message.
     * </p>
     *
     * @return a list of all `Product` entities
     * @throws ExceptionClass if an error occurs during the retrieval process
     */
    @Override
    public List<Product> getAllProducts() throws ExceptionClass {
        try {
            return productRepository.findAll();
        } catch (Exception e) {
            throw new ExceptionClass("Error while getting the products: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a product by its unique ID.
     *
     * <p>This method removes the specified product from the database.
     * If an error occurs during the deletion process, an `ExceptionClass` is thrown with a relevant error message.
     * </p>
     *
     * @param id : the unique identifier of the product to delete
     * @throws ExceptionClass : if an error occurs during the deletion process
     */
    @Override
    public void deleteProductById(Long id) throws ExceptionClass {
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            throw new ExceptionClass("Error while deleting the product with ID: " + id, e);
        }
    }

    /**
     * Updates the details of an existing product by its unique ID.
     *
     * <p>This method retrieves the product specified by {@code productId}, and updates its properties based on the provided {@link ProductDto} object.
     * If a property in {@code updatedProduct} is null or zero (for numeric fields), the existing property value is retained.
     * If the product is not found, or if an error occurs during the update process, an `ExceptionClass` is thrown with a relevant error message.
     * </p>
     *
     * @param productId : the unique identifier of the product to update
     * @param updatedProduct : a {@link ProductDto} object containing the updated product details
     * @return the updated {@link Product} object after saving changes to the database
     * @throws ExceptionClass if an error occurs during the update process, or if the product with the specified ID is not found
     *
     */
    @Override
    public Product updateProductById(Long productId, ProductDto updatedProduct) throws ExceptionClass {
        try {
            Product existingProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new ExceptionClass("Product not found with ID: " + productId));

            existingProduct.setCode(updatedProduct.getCode() != null ? updatedProduct.getCode() : existingProduct.getCode());
            existingProduct.setName(updatedProduct.getName() != null ? updatedProduct.getName() : existingProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription() != null ? updatedProduct.getDescription() : existingProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice() != null ? updatedProduct.getPrice() : existingProduct.getPrice());
            existingProduct.setQuantity(updatedProduct.getQuantity() != 0 ? updatedProduct.getQuantity() : existingProduct.getQuantity());
            existingProduct.setInternalReference(updatedProduct.getInternalReference() != null ? updatedProduct.getInternalReference() : existingProduct.getInternalReference());
            existingProduct.setInventoryStatus(updatedProduct.getInventoryStatus() != null ? updatedProduct.getInventoryStatus() : existingProduct.getInventoryStatus());
            existingProduct.setRating(updatedProduct.getRating() != 0.0 ? updatedProduct.getRating() : existingProduct.getRating());
            existingProduct.setCategory(updatedProduct.getCategoryId() != null ? categoryRepository.findById(updatedProduct.getCategoryId()).orElseThrow() : existingProduct.getCategory());

            return productRepository.save(existingProduct);
        } catch (Exception e) {
            throw new ExceptionClass("Error while updating the product with ID: " + productId, e);
        }
    }

    /**
     * Updates the image of an existing product by its unique ID.
     *
     * <p>This method retrieves the product specified by {@code productId} and updates its image based on the provided {@link MultipartFile} object.
     * If the image upload is successful, the new image path is saved; otherwise, the existing image path is retained.
     * If the product is not found, or if an error occurs during the update process, an `ExceptionClass` is thrown with a relevant error message.
     * </p>
     *
     * @param productId : the unique identifier of the product whose image will be updated
     * @param image : a {@link MultipartFile} containing the new image for the product
     * @return the updated {@link Product} object with the new image path after saving changes to the database
     * @throws ExceptionClass : if an error occurs during the image update process, or if the product with the specified ID is not found
     *
     */
    @Override
    public Product updateImageProductById(Long productId, MultipartFile image) throws ExceptionClass {
        try {
            Product existingProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new ExceptionClass("Product not found with ID: " + productId));

            String filePath = saveImage(image);
            existingProduct.setImage(filePath);
            return productRepository.save(existingProduct);
        } catch (Exception e) {
            throw new ExceptionClass("Error while updating the product image with ID: " + productId, e);
        }
    }

    /**
     * Retrieves the image of a product by its unique ID as a byte array.
     *
     * <p>This method fetches the product specified by {@code id} and returns the product's image as a byte array.
     * The image file is read from the path stored in the product's {@code image} attribute.
     * If the product is not found, or if an error occurs while reading the image file, an `ExceptionClass` is thrown with a relevant error message.
     * </p>
     *
     * @param id : the unique identifier of the product whose image is to be retrieved
     * @return a byte array containing the image data of the specified product
     * @throws ExceptionClass if the product with the specified ID is not found, or if an error occurs while reading the image file
     *
     */
    @Override
    public byte[] getProductImageById(Long id) throws ExceptionClass {
        try {
            Product product = productRepository.findById(id).orElseThrow(() -> new ExceptionClass("Product not found with ID: " + id));
            return Files.readAllBytes(Path.of(URI.create(product.getImage())));
        } catch (Exception e) {
            throw new ExceptionClass("Error while getting the product image with ID: " + id, e);
        }
    }


    /**
     * Saves the uploaded image to the specified storage directory.
     *
     * <p>
     * This method creates the storage directory if it does not exist, generates a unique filename
     * by appending a UUID to the original filename, and saves the file in the storage location.
     * It then returns the full path of the saved image file.
     * </p>
     *
     * @param file the MultipartFile representing the uploaded image file
     * @return the relative path to the saved image file within the storage directory
     * @throws ExceptionClass if an error occurs during file upload, such as directory creation or file saving issues
     *
     */
    private String saveImage(MultipartFile file) throws ExceptionClass {
        try {
            Path folderPath = Paths.get(storageDirectory).toAbsolutePath();
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "_" + originalFilename;
            Path filePath = folderPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            return storageDirectory + "/" + fileName;
        } catch (Exception e){
            throw new ExceptionClass("Error while uploading the file: " + e.getMessage(), e);
        }
    }
}