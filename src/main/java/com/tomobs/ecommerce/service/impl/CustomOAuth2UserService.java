package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.enums.RoleEnum;
import com.tomobs.ecommerce.model.Role;
import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.repository.RoleRepository;
import com.tomobs.ecommerce.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomOAuth2UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User auth2User = super.loadUser(userRequest);

        String email = auth2User.getAttribute("email");

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            User newUser = new User();
            newUser.setUserName(auth2User.getAttribute("name"));
            newUser.setEmail(email);
            Role defaultRole = roleRepository.findByRoleName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Default role not found!"));
            newUser.setRole(defaultRole);
            user = userRepository.save(newUser);
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getRoleName().name())),
                auth2User.getAttributes(),
                "email");
    }
}
