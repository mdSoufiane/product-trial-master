package com.alten.back.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity @NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @Embedded
    private AuditInfo auditInfo;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products;
}
