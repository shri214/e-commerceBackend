package com.ecommerce.ecommerce.Services;

import com.ecommerce.ecommerce.Entity.User;
import com.ecommerce.ecommerce.Reposetory.UserReposeteries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailServices implements UserDetailsService {

    @Autowired
    private UserReposeteries userReposeteries;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser=userReposeteries.findByUserName(username);
        User user=null;
        if(optionalUser.isPresent()){
            user=optionalUser.get();
            if (user==null){
                throw new UsernameNotFoundException("user not found");
            }
        }
        return new MyPricipleDetails(user);
    }
}
