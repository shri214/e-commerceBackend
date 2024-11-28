package com.ecommerce.ecommerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ProductDto {
    private String id;
    private String name;
    private double price;
    private String description;
    private List<ProductAssets> assets;

    public ProductDto(Product product){
        this.id=product.getId();
        this.name=product.getName();
        this.price=product.getPrice();
        this.description=product.getDescription();
        this.assets=product.getAssets();
    }
}
