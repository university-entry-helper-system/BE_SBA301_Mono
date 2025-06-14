package com.example.SBA_M.init;

import com.example.SBA_M.utils.RoleName;
import com.example.SBA_M.entity.Role;
import com.example.SBA_M.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        for (Role.RoleName roleName : Role.RoleName.values()) {
            if (!roleRepository.existsByName(roleName)) {
                Role role = new Role();
                role.setName(roleName);
                role.setDescription("Default role: " + roleName.name());
                roleRepository.save(role);
            }
        }
    }
}
