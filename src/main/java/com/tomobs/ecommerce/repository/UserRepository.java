package com.tomobs.ecommerce.repository;

// MAIN INTERFACE FOR SAVING AND FETCHING USER OBJECTS FROM THE DATABASE.
import com.tomobs.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{

    // METHOD FOR CHECKING EMAIL EXISTENCE IN SERVICE LOGIC BEFORE THE SIGNUP
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
