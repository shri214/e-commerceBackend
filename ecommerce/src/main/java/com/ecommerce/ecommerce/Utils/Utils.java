package com.ecommerce.ecommerce.Utils;

import com.ecommerce.ecommerce.Entity.User;
import com.ecommerce.ecommerce.Services.MyPricipleDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class Utils {
    public static Optional<User> getCurrentUsers(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyPricipleDetails principal = (MyPricipleDetails) authentication.getPrincipal();
        Optional<User>u= Optional.ofNullable(principal.getUserObject());
        return u;
    }
}
