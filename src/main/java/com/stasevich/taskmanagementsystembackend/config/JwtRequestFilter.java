package com.stasevich.taskmanagementsystembackend.config;

import com.stasevich.taskmanagementsystembackend.user.models.User;
import com.stasevich.taskmanagementsystembackend.user.util.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")){
            jwtToken = authHeader.substring(7);
            try {
                email = jwtTokenUtils.getUserEmailFromToken(jwtToken);
            } catch (ExpiredJwtException e) {
                log.debug("JWT token expired");
            }
        }

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    jwtTokenUtils.getRolesFromToken(jwtToken).stream()
                            .map(SimpleGrantedAuthority::new).toList()
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(request, response);
    }
}
