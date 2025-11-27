package com.tomobs.ecommerce.config;

import com.tomobs.ecommerce.entity.Role;
import com.tomobs.ecommerce.enums.RoleEnum;
import com.tomobs.ecommerce.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataIntializer {

    private final RoleRepository roleRepository;

    public DataIntializer(RoleRepository roleRepository) {

        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initilizeRoles() {

        if(roleRepository.count() == 0) {

            roleRepository.save(new Role(null, RoleEnum.ROLE_USER));
            roleRepository.save(new Role(null, RoleEnum.ROLE_ADMIN));

        }
    }
}
