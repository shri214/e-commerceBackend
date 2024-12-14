package com.ecommerce.ecommerce.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAssets {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private String id;
    @Lob
    private byte[] image;

    @ManyToOne(fetch = FetchType.EAGER
    )
    @JoinColumn(name = "product_id")
    @JsonBackReference
    @ToString.Exclude
    private Product product;

    @PrePersist
    public void generatedId(){
        this.id= UUID.randomUUID().toString();
    }
}
