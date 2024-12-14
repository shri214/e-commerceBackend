package com.ecommerce.ecommerce.Controller;

import com.ecommerce.ecommerce.Entity.ProductResponse;
import com.ecommerce.ecommerce.GlobalError.UserAlreadyExitsException;
import com.ecommerce.ecommerce.Services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/product")
public class productController {
    @Autowired
    private ProductServices productServices;

    @GetMapping
    public ResponseEntity<ProductResponse> getProduct(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            return productServices.getProduct(page, size);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @GetMapping("/product-id/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id) {
        try {
            return productServices.getProductById(id);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    @PostMapping(value = "/addProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, String>> addNewProduct(
            @RequestParam("product") String data,
            @RequestParam("image") MultipartFile[] file) {
        try {
            productServices.addNewProduct(data, file);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Product added successfully :)");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (UserAlreadyExitsException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (Exception ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while adding the product");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<HashMap<String, String>> deleteById(@PathVariable String id){
        try{
            HashMap<String,String>response=new HashMap<>();
            productServices.delete(id);
            response.put("message", "deleted success fully done");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (RuntimeException ex){
            HashMap<String,String>response=new HashMap<>();
            response.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(value = "/addingAsset/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, String>> addingAsset(@PathVariable String id, @RequestParam ("image") MultipartFile file){
         try {
             Map<String, String> response=new HashMap<>();
             productServices.saveAsset(id , file);
             response.put("response","asssets adding success ful");
             return ResponseEntity.status(HttpStatus.OK).body(response);
         }catch (RuntimeException ex){
             Map<String, String> response=new HashMap<>();
             response.put("error", "somthing error"+ex);
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
         }
    }

}
