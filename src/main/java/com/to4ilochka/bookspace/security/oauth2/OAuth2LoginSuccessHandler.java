package com.to4ilochka.bookspace.security.oauth2;

import com.to4ilochka.bookspace.dto.auth.AuthResponse;
import com.to4ilochka.bookspace.security.jwt.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        AuthResponse authResponse = jwtService.generateAuthToken(email);

        String targetUrl = "http://localhost:5173/oauth2/redirect?accessToken="
                + authResponse.accessToken()
                + "&refreshToken="
                + authResponse.refreshToken();

        response.sendRedirect(targetUrl);
    }
}