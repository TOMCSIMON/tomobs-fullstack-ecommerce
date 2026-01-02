package com.tomobs.ecommerce.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler  implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        log.info("LOGIN SUCCESS | Roles = {}", authentication.getAuthorities());

        // Get the role of the logged-in user
        boolean isAdmin = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        boolean isUser = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_USER"));

        // Role-based redirect
        if (isAdmin) {
            response.sendRedirect("/admin/dashboard");
        } else if (isUser) {
            response.sendRedirect("/");
        } else {
            response.sendRedirect("/login?error");
        }

    }
}
