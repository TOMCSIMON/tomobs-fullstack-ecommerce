package com.tomobs.ecommerce.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof LockedException ||
                (exception instanceof InternalAuthenticationServiceException && exception.getCause() instanceof LockedException)){
            getRedirectStrategy().sendRedirect(request, response, "/blocked-page");
        }else {
            getRedirectStrategy().sendRedirect(request, response, "/login?error=true");
        }
    }
}
