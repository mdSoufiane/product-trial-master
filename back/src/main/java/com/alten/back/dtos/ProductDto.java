package com.alten.back.dtos;

import com.alten.back.enums.InventoryStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder
public class ProductDto {

    @NotEmpty(message = "Code cannot be empty")
    @NotNull(message = "Code cannot be null")
    private String code;

    @NotEmpty(message = "Name cannot be empty")
    @NotNull(message = "Name cannot be null")
    private String name;

    @NotEmpty(message = "Description cannot be empty")
    @NotNull(message = "Description cannot be null")
    private String description;

    @Positive(message = "You should insert a positive price number")
    private Double price;

    @PositiveOrZero(message = "Quantity must be more or equal than zero")
    private int quantity;

    @NotEmpty(message = "internal reference cannot be empty")
    @NotNull(message = "internal reference cannot be null")
    private String internalReference;

    @NotEmpty(message = "inventory status cannot be empty")
    @NotNull(message = "inventory status cannot be null")
    private InventoryStatus inventoryStatus;

    @Min(value = 1, message = "Rating must be a number between 1 and 5")
    @Max(value = 5, message = "Rating must be a number between 1 and 5")
    private double rating;

    private Long categoryId;
}
