package com.ecommerce.ecommerce.Services;

import com.ecommerce.ecommerce.Entity.Cart;
import com.ecommerce.ecommerce.Entity.Credential;
import com.ecommerce.ecommerce.Entity.User;
import com.ecommerce.ecommerce.Entity.UserDto;
import com.ecommerce.ecommerce.Filter.JwtServices;
import com.ecommerce.ecommerce.GlobalError.UserAlreadyExitsException;
import com.ecommerce.ecommerce.Reposetory.CartRepo;
import com.ecommerce.ecommerce.Reposetory.UserReposeteries;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServices {
    @Autowired
    private UserReposeteries userReposeteries;

    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private JwtServices jwtServices;
    @Autowired
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Authentication authentication(String userName, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        return authentication;
    }

    public User addNewUser(User user, boolean isAdmin) {
        Optional<User> optionalUser = userReposeteries.findByUserName(user.getUserName());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExitsException("user already exists: " + user.getUserName());
        }

        if (!validateUser(user)) {
            throw new RuntimeException("Provided data is not enough");
        }
        user.setRole(isAdmin ? "ADMIN" : "USER");

        user.setPassword(encoder.encode(user.getPassword()));

        userReposeteries.save(user);

        User savedUser = userReposeteries.findByUserName(user.getUserName()).orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = new Cart();
        cart.setUser(savedUser);
        cartRepo.save(cart);

        return savedUser;
    }


    public UserDto loginUser(Credential user) {

        Optional<User> optionalUser = userReposeteries.findByUserName(user.getUserName());

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

//            if (encoder.matches(user.getPassword(), existingUser.getPassword())) {
//                return "login success full " + Verify(user);
//            }
            Authentication a = authentication(user.getUserName(), user.getPassword());
            if (a.isAuthenticated()){
//                String token= jwtServices.getJsonToken(user.getUserName());
                String role=existingUser.getRole();
                String userId=existingUser.getId();
                System.out.println("user id "+ userId);
                return new UserDto(jwtServices.getJsonToken(user.getUserName()), user.getUserName(), role, userId);
            }
            throw new RuntimeException("Login failed: invalid credentials.");
        }else{
            throw new RuntimeException("Login failed: user not found.");
        }


    }

    private boolean validateUser(User user) {
        if (user.getEmail() == null || user.getUserName() == null || user.getPassword() == null) {
            return false;
        }
        return user.getPassword().length() >= 8;
    }

//    public String Verify(Credential user) {
//        Authentication authentication =
//                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
//        if (authentication.isAuthenticated()) {
//            return jwtServices.getJsonToken(user.getUserName());
//        }
//        return "fail";
//    }


}
