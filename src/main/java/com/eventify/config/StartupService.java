package com.eventify.config;

import com.eventify.models.Role;
import com.eventify.models.RoleType;
import com.eventify.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupService implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public StartupService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findByName(RoleType.ROLE_USER).isEmpty()) {
            roleRepository.save(new Role(null, RoleType.ROLE_USER));
        }
        if (roleRepository.findByName(RoleType.ROLE_ORGANIZER).isEmpty()) {
            roleRepository.save(new Role(null, RoleType.ROLE_ORGANIZER));
        }
        if (roleRepository.findByName(RoleType.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(null, RoleType.ROLE_ADMIN));
        }
        System.out.println("✅ Roles Seeded Successfully");
    }
}
