package com.tomobs.ecommerce.config;

import com.tomobs.ecommerce.model.Role;
import com.tomobs.ecommerce.enums.RoleEnum;
import com.tomobs.ecommerce.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {

        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initializeRoles() {

        if(roleRepository.count() == 0) {

            roleRepository.save(new Role(null, RoleEnum.ROLE_USER));
            roleRepository.save(new Role(null, RoleEnum.ROLE_ADMIN));

        }
    }
}
