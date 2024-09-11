package com.Optimart.Initializer;

import com.Optimart.constants.Permissions;
import com.Optimart.models.Role;
import com.Optimart.models.User;
import com.Optimart.repositories.RoleRepository;
import com.Optimart.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RoleUserInitializer {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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
                basicRole.setPermissions(List.of(Permissions.BASIC));
                roleRepository.save(basicRole);

                User adminUser = User.builder()
                        .email("admin@gmail.com")
                        .password("Admin@123")
                        .status(1)
                        .userType(1)
                        .userName("admin@gmail.com")
                        .role(adminRole).build();
                String encodedAdminPassword = passwordEncoder.encode("Admin@123");
                adminUser.setPassword(encodedAdminPassword);
                userRepository.save(adminUser);

                User basicUser = User.builder()
                        .email("client@gmail.com")
                        .fullName("Phạm Nguyên Tú")
                        .firstName("Tú")
                        .middleName("Nguyên")
                        .lastName("Phạm")
                        .phoneNumber("0934145609")
                        .password("Client@123")
                        .status(1)
                        .userType(1)
                        .userName("client@gmail.com")
                        .role(basicRole).build();
                String encodedClientPassword = passwordEncoder.encode("Client@123");
                basicUser.setPassword(encodedClientPassword);
                userRepository.save(basicUser);
                System.out.println("Roles and user have been initialized.");
            }
        };
    }
}
