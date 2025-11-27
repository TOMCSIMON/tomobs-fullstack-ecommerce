package com.tomobs.ecommerce.repository;

// MAIN INTERFACE FOR SAVING AND FETCHING USER OBJECTS FROM THE DATABASE.
import com.tomobs.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

    // METHOD FOR CHECKING EMAIL EXISTENCE IN SERVICE LOGIC BEFORE THE SIGNUP
    boolean existsByEmail(String email);
}
