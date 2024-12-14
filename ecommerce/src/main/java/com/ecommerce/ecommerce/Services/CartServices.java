package com.ecommerce.ecommerce.Services;

import com.ecommerce.ecommerce.Entity.Cart;
import com.ecommerce.ecommerce.Entity.CartItem;
import com.ecommerce.ecommerce.Entity.Product;
import com.ecommerce.ecommerce.Entity.User;
import com.ecommerce.ecommerce.Reposetory.CartRepo;
import com.ecommerce.ecommerce.Reposetory.ProductRepo;
import com.ecommerce.ecommerce.Utils.Utils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServices {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ProductRepo productRepo;


    @Transactional
    public ResponseEntity<Cart> addToCart(String userId, String productId, int quantity) {
        Optional<Cart> cartOpt = cartRepo.findByUser_Id(userId);
        Optional<Product> productOpt = productRepo.findById(productId);

        if (!cartOpt.isPresent()) {
            throw new RuntimeException("Cart not found for user");
        }

        if (!productOpt.isPresent()) {
            throw new RuntimeException("Product not found");
        }

        Cart cart = cartOpt.get();
        Product product = productOpt.get();

        Optional<CartItem> existingCartItemOpt = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingCartItemOpt.isPresent()) {
            CartItem existingCartItem = existingCartItemOpt.get();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            newCartItem.setCart(cart);
            cart.getItems().add(newCartItem);
        }

        cartRepo.save(cart);
        return ResponseEntity.ok().body(cart);
    }

    public ResponseEntity<Cart> removeFromCart(String userId, String productId, Integer quantity) {
        Optional<Cart> cartOpt = cartRepo.findByUser_Id(userId);

        if (!cartOpt.isPresent()) {
            throw new RuntimeException("Cart not found for user");
        }

        Cart cart = cartOpt.get();
        System.out.println(cart.getItems());

        Optional<CartItem> cartItemOpt = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (!cartItemOpt.isPresent()) {
            throw new RuntimeException("Product not found in cart");
        }

        CartItem cartItem = cartItemOpt.get();

        if (quantity == null || quantity <= 0) {
            cartRepo.deleteCartItemById(cartItem.getId());
        } else {
            if (cartItem.getQuantity() > quantity) {
                cartItem.setQuantity(cartItem.getQuantity() - quantity);
            } else {
                cartRepo.deleteCartItemById(cartItem.getId());
            }
        }

        cartRepo.save(cart);
        return ResponseEntity.ok().body(cart);
    }

    public ResponseEntity<?> getCartDetails() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyPricipleDetails principal = (MyPricipleDetails) authentication.getPrincipal();
        String userIds = principal.getUserId();

        Optional<User> u= Utils.getCurrentUsers();

        Optional<Cart> cartOpt = cartRepo.findByUser_Id(userIds);

        if (!cartOpt.isPresent()) {
            throw new RuntimeException("Cart not found for user");
        }
        return ResponseEntity.ok().body(cartOpt.get().getItems());

//        Pageable pageable = PageRequest.of(page, size);
//
//        Page<Cart> cartPage=cartRepo.findByUser_Id(pageable, userIds);
//
//        if (cartPage.isEmpty()) {
//            throw new RuntimeException("Cart not found for user");
//        }
//        System.out.println(cartPage.getContent().stream());

    }

    public List<Cart> getCartItems() {
        try {
            return cartRepo.findAll();
        } catch (Exception ex) {
            throw new RuntimeException("something wrong");
        }
    }
}
