package com.ecommerce.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order_tables")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @Column(nullable = false, unique = true, length = 100, updatable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    private double totalAmount;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID().toString();
    }

}

