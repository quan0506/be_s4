package com.fptaptech.s4.service.impl;

//import com.fptaptech.s4.entity.ConfirmationToken;
import com.fptaptech.s4.entity.Role;
import com.fptaptech.s4.entity.User;
import com.fptaptech.s4.exception.UserAlreadyExistsException;
import com.fptaptech.s4.repository.ConfirmationTokenRepository;
import com.fptaptech.s4.repository.RoleRepository;
import com.fptaptech.s4.repository.UserRepository;
import com.fptaptech.s4.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
/*import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;*/
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
    /*private final ConfirmationTokenRepository confirmationTokenRepository;*/
    /*private final EmailService emailService;*/

    @Override
    public User registerUser(User user, String roleName) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = findRoleByName(roleName);
        user.setRoles(Collections.singletonList(role));
        user.setEnabled(false);
        User registeredUser = userRepository.save(user);

        /*sendConfirmationEmail(user);*/

        return registeredUser;
    }

    private Role findRoleByName(String roleName) {
        return switch (roleName.toUpperCase()) {
            case "ADMIN" -> roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
            case "USER" -> roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("User role not found"));
            default -> roleRepository.findByName("ROLE_EMPLOYEE")
                    .orElseThrow(() -> new RuntimeException("Employee role not found"));
        };
    }

    /*private void sendConfirmationEmail(User user) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:4444/confirm-account?token=" + confirmationToken.getConfirmationToken());
        emailService.sendEmail(mailMessage);
    }*/

    /*public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository
                .findByConfirmationToken(confirmationToken);
        if (token != null) {
            User user = userRepository.findByEmail(token.getUser().getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            user.setEnabled(true);
            userRepository.save(user);
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }*/

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        User theUser = getUser(email);
        if (theUser != null) {
            userRepository.deleteByEmail(email);
        }
    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
