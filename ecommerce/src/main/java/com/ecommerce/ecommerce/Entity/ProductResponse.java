package com.ecommerce.ecommerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ProductResponse {
    private String status;
    private List<ProductDto> data;
    private String message;
    private int totalPage;
    private int size;

    public ProductResponse(String status, List<ProductDto> data, String message, int totalPage, int size) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.totalPage=totalPage;
        this.size=size;
    }
    public ProductResponse(String status, List<ProductDto> data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }
}
