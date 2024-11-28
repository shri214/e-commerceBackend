package com.ecommerce.ecommerce.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Range;

import java.util.List;
import java.util.UUID;


@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @Column(nullable = false, unique = true, length = 100, updatable = false)
    private String id;
    private String name;
    private double price;
    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<ProductAssets> assets;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID().toString();
    }

}
