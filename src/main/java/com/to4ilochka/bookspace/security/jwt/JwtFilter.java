package com.to4ilochka.bookspace.security.jwt;

import com.to4ilochka.bookspace.security.CustomUserDetails;
import com.to4ilochka.bookspace.security.CustomUserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserServiceImpl customUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if (token != null && jwtService.validateJwtToken(token)) {
            setCustomUserDetailsToSecurityContextHolder(token);
        }

        filterChain.doFilter(request, response);
    }

    private void setCustomUserDetailsToSecurityContextHolder(String token) {
        String email = jwtService.getEmailFromToken(token);
        CustomUserDetails customUserDetails = customUserService.loadUserByUsername(email);

        if (customUserDetails.isAccountNonLocked() && customUserDetails.isEnabled()) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    customUserDetails,
                    null,
                    customUserDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
