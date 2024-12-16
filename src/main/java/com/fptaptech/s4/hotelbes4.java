package com.fptaptech.s4;

import com.fptaptech.s4.entity.Role;
import com.fptaptech.s4.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class hotelbes4 {

    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(hotelbes4.class, args);
    }

    @Bean
    CommandLineRunner initRoles() {
        return args -> {
            if (!roleRepository.existsByName("ROLE_MANAGER")) {
                Role adminRole = new Role();
                adminRole.setName("ROLE_MANAGER");
                roleRepository.save(adminRole);
                System.out.println("Role MANAGER has been created");
            }
        };
    }
}
