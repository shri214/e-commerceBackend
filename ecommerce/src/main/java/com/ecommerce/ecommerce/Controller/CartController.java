package com.ecommerce.ecommerce.Controller;

import com.ecommerce.ecommerce.Entity.Cart;
import com.ecommerce.ecommerce.Entity.User;
import com.ecommerce.ecommerce.Services.CartServices;
import com.ecommerce.ecommerce.Utils.Utils;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartServices cartServices;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Cart>> getCartItem() {
        try {
            List<Cart> carts = cartServices.getCartItems();
            return ResponseEntity.status(HttpStatus.OK).body(carts);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @PostMapping("/addToCart")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Cart> addCartItem(@RequestHeader String userId, @RequestHeader String productId, @RequestHeader int quantity) {

        Optional<User> u= Utils.getCurrentUsers();
        if(u.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            ResponseEntity<Cart> cart = cartServices.addToCart(userId, productId, quantity);
            return ResponseEntity.ok().body(cart.getBody());
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @PostMapping("/removeToCart")
    public ResponseEntity<String> removeToCart(@RequestHeader String userId, @RequestHeader String productId, @RequestHeader int quantity){
        try {
            Integer quantities= quantity;
            ResponseEntity<Cart> cart =cartServices.removeFromCart(userId, productId, quantities);
            return ResponseEntity.ok().body("item remove success fully");
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @GetMapping("/getCartDetails")
    public ResponseEntity<?> getCart(){
        try {
            ResponseEntity<?> cart= cartServices.getCartDetails();
            return ResponseEntity.status(HttpStatus.OK).body(cart);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
