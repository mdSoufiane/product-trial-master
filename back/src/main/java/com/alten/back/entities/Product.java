package com.alten.back.entities;

import com.alten.back.enums.InventoryStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString @Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    private String image;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private int quantity;

    @Column(unique = true, nullable = false)
    private String internalReference;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InventoryStatus inventoryStatus;

    private double rating;

    private int shellId;

    @ManyToOne
    private Category category;

    @Embedded
    private AuditInfo auditInfo;


}
