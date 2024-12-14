package com.ecommerce.ecommerce.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private String userName;
    private String token;
    private String role;
    private String userId;

    public UserDto(String token, String userName, String role, String userId){
       this.token=token;
       this.userName=userName;
       this.role=role;
       this.userId=userId;
    }
}
