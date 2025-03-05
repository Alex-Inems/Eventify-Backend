package com.eventify.services;

import com.eventify.models.Role;
import com.eventify.models.RoleType;
import com.eventify.models.User;
import com.eventify.repositories.RoleRepository;
import com.eventify.repositories.UserRepository;
import com.eventify.security.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository; // ✅ Added RoleRepository
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository; // ✅ Injected RoleRepository
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return jwtUtils.generateToken(email);
        }
        throw new RuntimeException("Invalid email or password");
    }

    public void register(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User already exists");
        }
    
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
    
        // ✅ Assign default role USER
        Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    
        user.getRoles().add(userRole);
    
        userRepository.save(user);
        System.out.println("Saving user: " + user.getEmail());

    }
}
