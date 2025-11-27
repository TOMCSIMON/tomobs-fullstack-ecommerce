package com.tomobs.ecommerce.repository;

// THE INTERFACE THAT ALLOWS  APPLICATION TO FETCH ROLES FROM THE DATABASE.
import com.tomobs.ecommerce.entity.Role;
import com.tomobs.ecommerce.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleEnum roleEnum);
}
