package com.ecommerce.ecommerce.Reposetory;

import com.ecommerce.ecommerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserReposeteries extends JpaRepository<User, String> {
    Optional<User> findByUserName(String user);
}
