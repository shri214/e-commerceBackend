package com.ecommerce.ecommerce.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private String userName;
    private String token;
    private String role;

    public UserDto(String token, String userName, String role){
       this.token=token;
       this.userName=userName;
       this.role=role;
    }
}
