package com.fptaptech.s4.service;

import com.fptaptech.s4.exception.UserAlreadyExistsException;
import com.fptaptech.s4.entity.Role;
import com.fptaptech.s4.entity.User;
import com.fptaptech.s4.repository.RoleRepository;
import com.fptaptech.s4.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public User registerUser(User user,String roleName) {
        if (userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        Role role;
        if ("ADMIN".equalsIgnoreCase(roleName)) {
            role = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
        } else if("USER".equalsIgnoreCase(roleName)) {
            role = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("User role not found"));
        } else  {
            role = roleRepository.findByName("ROLE_EMPLOYEE")
                    .orElseThrow(() -> new RuntimeException("Employee role not found"));
        }

        user.setRoles(Collections.singletonList(role));
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        User theUser = getUser(email);
        if (theUser != null){
            userRepository.deleteByEmail(email);
        }
    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
