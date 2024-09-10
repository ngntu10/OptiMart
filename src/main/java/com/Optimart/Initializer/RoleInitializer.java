package com.Optimart;

import com.Optimart.constants.Permissions;
import com.Optimart.models.Role;
import com.Optimart.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RoleInitializer {

    @Autowired
    private final RoleRepository roleRepository;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            if (roleRepository.count() == 0) {
                Role adminRole = new Role();
                adminRole.setName("ADMIN");
                adminRole.setPermissions(List.of(Permissions.ADMIN));
                roleRepository.save(adminRole);

                Role basicRole = new Role();
                basicRole.setName("BASIC");
                adminRole.setPermissions(List.of(Permissions.BASIC));
                roleRepository.save(basicRole);

                System.out.println("Roles have been initialized.");
            }
        };
    }
}
