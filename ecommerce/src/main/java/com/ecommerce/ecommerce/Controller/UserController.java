package com.ecommerce.ecommerce.Controller;

import com.ecommerce.ecommerce.Entity.Credential;
import com.ecommerce.ecommerce.Entity.User;
import com.ecommerce.ecommerce.Entity.UserDto;
import com.ecommerce.ecommerce.GlobalError.UserAlreadyExitsException;
import com.ecommerce.ecommerce.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

@Autowired
private UserServices userServices;

    @Value("${admin.secret.key}")
    private String adminSecretKey;

    @GetMapping
    public String healthCheck(){

        return "ok";
    }

    @PostMapping("/signUp")
    public String SignUp(@RequestBody User user, @RequestParam(defaultValue = "false") boolean isAdmin,
    @RequestHeader(value = "Admin-Authorization", required = false) String adminKey) {
        try {
            if(isAdmin){
//                String adminSecretKey= System.getenv("admin.secret.key");
                if (adminKey == null || !adminKey.equals(adminSecretKey)) {
                    return "Unauthorized to create an admin user";
                }

            }
            userServices.addNewUser(user, isAdmin);
            return "you are sign up success fully ";
        }catch (UserAlreadyExitsException ex){
            throw  ex;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Credential user){
        try {
            Map<String, String> authToken=new HashMap<>();
            UserDto userDto = userServices.loginUser(user);
            authToken.put("token", userDto.getToken());
            authToken.put("userName", userDto.getUserName());
            authToken.put("role", userDto.getRole());
            authToken.put("userId",userDto.getUserId());
            return ResponseEntity.ok(authToken);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
    }
