package com.ecommerce.ecommerce.Services;

import com.ecommerce.ecommerce.Entity.*;
import com.ecommerce.ecommerce.GlobalError.UserAlreadyExitsException;
import com.ecommerce.ecommerce.GlobalError.ValidationException;
import com.ecommerce.ecommerce.Reposetory.ProductAssetsRepo;
import com.ecommerce.ecommerce.Reposetory.ProductRepo;
import com.ecommerce.ecommerce.Utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServices {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
   private ProductAssetsRepo productAssetsRepo;

    public ResponseEntity<ProductResponse>getProduct(int page, int size) {
        Pageable pageable= PageRequest.of(page, size);
        Page<Product> productPage=productRepo.findAll(pageable);
//        Optional<User> u=Utils.getCurrentUsers();
//        if(u.isPresent()){
//            System.out.println(u.get());
//        }
        if (productPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<ProductDto> productDtos=productPage.getContent().stream().map(ProductDto::new).toList();
        ProductResponse response = new ProductResponse("success", productDtos, "Products retrieved successfully", productPage.getTotalPages(), productPage.getSize());
        System.out.println(response);
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<ProductResponse> getProductById(String id){
        try {
            Product product=productRepo.findById(id).orElseThrow(()->new RuntimeException("product id ont matched"));
                ProductDto productDtos=new ProductDto(product);
                ProductResponse response = new ProductResponse("success", List.of(productDtos), "Products retrieved successfully");
                return ResponseEntity.ok().body(response);

        }catch (Exception e){
            throw new RuntimeException("something issue occurred");
        }

    }

    public ResponseEntity<Product> addNewProduct(String data, MultipartFile[] file) {

        if (!StringUtils.hasText(data))
            throw new ValidationException("Data part is empty");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Product product = objectMapper.readValue(data, Product.class);
            if (validateProduct(product)) {
                Optional<Product> optionalProduct = productRepo.findByName(product.getName());
                if (optionalProduct.isPresent()) {
                    throw new UserAlreadyExitsException("Product already exists; cannot add duplicate product.");
                }
//                product.setImage(file.getBytes());
                List<ProductAssets> productAssets=new ArrayList<>();
                for (MultipartFile files:file){
                    ProductAssets assets=new ProductAssets();
                    assets.setImage(files.getBytes());
                    assets.setProduct(product);
                    productAssets.add(assets);
                }
                product.setAssets(productAssets);
                Product savedProduct = productRepo.save(product);
                return ResponseEntity.ok(savedProduct);
            }
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    public void saveAsset(ProductAssets productAssets){
        try {
           ProductAssets p= productAssetsRepo.save(productAssets);
            System.out.println(p.getId());
        }catch (Exception ex){
            throw new RuntimeException("issue occurred while saving assest");
        }
    }
    public boolean validateProduct(Product product) {
        if (product.getName() == null || product.getDescription() == null || product.getDescription() == null) {
            return false;
        }
        return true;
    }

    public ResponseEntity<Product> deleteProductById(String id){
        Optional<User> u= Utils.getCurrentUsers();
        if(u.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Product product=productRepo.findById(id).orElseThrow(()->new RuntimeException("product not mateced"));
        try {
           productRepo.deleteById(id);
           return ResponseEntity.ok(product);
        }catch (Exception ex){
            throw new RuntimeException("some thing error occurred during deleting");
        }
    }
}

