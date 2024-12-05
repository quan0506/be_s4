package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.dto.ResetPasswordDTO;
import com.fptaptech.s4.dto.UserDTO;
import com.fptaptech.s4.dto.UserUpdateDTO;
import com.fptaptech.s4.entity.Role;
import com.fptaptech.s4.entity.User;
import com.fptaptech.s4.entity.VerificationCode;
import com.fptaptech.s4.exception.OurException;
import com.fptaptech.s4.exception.UserAlreadyExistsException;
import com.fptaptech.s4.repository.RoleRepository;
import com.fptaptech.s4.repository.UserRepository;
import com.fptaptech.s4.repository.VerificationCodeRepository;
import com.fptaptech.s4.service.interfaces.IUserService;
import com.fptaptech.s4.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;

        @Override
        public UserDTO findById(Long userId) {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            return Utils.mapUserEntityToUserDTO(user);
        }

    @Override public UserDTO findByEmail(String email) {
            User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new OurException("User Not Found"));
            return Utils.mapUserEntityToUserDTO(user); }


    @Override
    public User registerUser(User user, String roleName) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = findRoleByName(roleName);
        user.setRoles(Collections.singletonList(role));
        user.setEnabled(false);
//        User registeredUser = userRepository.save(user);

        /*sendConfirmationEmail(user);*/

        return  userRepository.save(user);
    }

    private Role findRoleByName(String roleName) {
        return switch (roleName.toUpperCase()) {
            case "ROLE_ADMIN" -> roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
            case "ROLE_USER" -> roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("User role not found"));
            default -> roleRepository.findByName("ROLE_EMPLOYEE")
                    .orElseThrow(() -> new RuntimeException("Employee role not found"));
        };
    }

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

    @Transactional
    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new OurException("User Not Found"));

        if (userUpdateDTO.getUserName() != null) {
            user.setEmail(userUpdateDTO.getUserName());
        }
        if (userUpdateDTO.getFirstName() != null) {
            user.setFirstName(userUpdateDTO.getFirstName());
        }
        if (userUpdateDTO.getLastName() != null) {
            user.setLastName(userUpdateDTO.getLastName());
        }
        if (userUpdateDTO.getPhone() != null) {
            user.setPhone(userUpdateDTO.getPhone());
        }
        if (userUpdateDTO.getEmail() != null) {
            user.setEmail(userUpdateDTO.getEmail());
        }
        if (userUpdateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }

        userRepository.save(user);
    }
        @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserDTO getCurrentUser(Authentication authentication) {
            String email = authentication.getName();
            // Lấy email từ thông tin người dùng đăng nhập
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new OurException("User Not Found"));
            return Utils.mapUserEntityToUserDTO(user); }

    public void sendVerificationCode(String email) {
        String code = generateVerificationCode();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        verificationCodeRepository.save(verificationCode);
        emailService.sendSimpleMessage(email, "Your verification code", "Your code is: " + code);
    }

    @Transactional
    public void resetPassword(String email, ResetPasswordDTO resetPasswordDTO) {
        // Lấy code từ resetPasswordDTO
        String code = resetPasswordDTO.getCode();

        VerificationCode verificationCode = verificationCodeRepository.findByEmailAndCode(email, code)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired verification code"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmNewPassword())) {
            throw new IllegalArgumentException("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        userRepository.save(user);
        verificationCodeRepository.delete(verificationCode);
    }

    private String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}

