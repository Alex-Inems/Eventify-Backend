package com.eventify.security;


import com.eventify.models.User;
import com.eventify.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public JwtFilter(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            if (jwtUtils.validateToken(token)) {
                String email = jwtUtils.extractEmail(token);
                Optional<User> userOptional = userRepository.findByEmail(email);

                if (userOptional.isPresent()) {
                    User user = userOptional.get();

                    List<String> roles = user.getRoles().stream()
                            .map(role -> role.getName().name()) // Convert RoleType to String
                            .collect(Collectors.toList());

                    UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                            .username(user.getEmail())
                            .password(user.getPassword())
                            .roles(roles.toArray(new String[0])) // Convert List to Array
                            .build();

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("JWT Filter Triggered");
System.out.println("Authorization Header: " + request.getHeader("Authorization"));
System.out.println("JWT Filter Triggered");
System.out.println("Authorization Header: " + request.getHeader("Authorization"));
System.out.println("Token: " + token);
System.out.println("Valid Token: " + jwtUtils.validateToken(token));
System.out.println("Extracted Email: " + jwtUtils.extractEmail(token));
System.out.println("User Present: " + userOptional.isPresent());
System.out.println("User Roles: " + roles);


                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
