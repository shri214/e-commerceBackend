package com.ecommerce.ecommerce.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @Column(nullable = false, unique = true, length = 100, updatable = false)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany( mappedBy = "cart", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private List<CartItem> items =new ArrayList<>();

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID().toString();
    }

}

