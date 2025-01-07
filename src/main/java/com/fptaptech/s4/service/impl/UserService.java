package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.dto.ResetPasswordDTO;
import com.fptaptech.s4.dto.Response;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
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
    public Response getUsers() {
        Response response = new Response();
        try {
            List<User> users = userRepository.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(users);
            response.setStatusCode(200);
            response.setMessage("Users retrieved successfully.");
            response.setData(userDTOList);
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error fetching users" + e.getMessage());
        }
        return response;
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
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new OurException("User Not Found"));
        return Utils.mapUserEntityToUserDTO(user);
    }

    @Override
    public void sendVerificationCode(String email) {
        String code = generateVerificationCode();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        verificationCodeRepository.save(verificationCode);

        // Tạo context cho template
        Context context = new Context();
        context.setVariable("code", code);

        // Gửi email xác nhận với template HTML
        emailService.sendHtmlMessage(email, "Your verification code", "verificationTemplate", context);

        logger.info("Verification code sent to email: {}", email);
    }

    @Transactional
    public void resetPassword(String email, ResetPasswordDTO resetPasswordDTO) {
        try {
            logger.info("Starting password reset process for email: {}", email);

            // Lấy code từ resetPasswordDTO
            String code = resetPasswordDTO.getCode();

            // Tìm mã xác thực
            VerificationCode verificationCode = verificationCodeRepository.findByEmailAndCode(email, code)
                    .orElseThrow(() -> {
                        logger.error("Invalid or expired verification code for email: {}", email);
                        return new IllegalArgumentException("Invalid or expired verification code");
                    });

            // Kiểm tra xem mã xác thực đã hết hạn chưa
            if (verificationCode.getExpiryDate().isBefore(LocalDateTime.now())) {
                logger.error("Verification code expired for email: {}", email);
                throw new IllegalArgumentException("Verification code expired");
            }

            // Tìm người dùng
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        logger.error("User not found for email: {}", email);
                        return new UsernameNotFoundException("User not found");
                    });

            // Kiểm tra xem mật khẩu mới có khớp nhau không
            if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmNewPassword())) {
                logger.error("New passwords do not match for email: {}", email);
                throw new IllegalArgumentException("New passwords do not match");
            }

            // Cập nhật mật khẩu mới cho người dùng
            user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
            userRepository.save(user);

            // Xóa mã xác thực sau khi sử dụng
            verificationCodeRepository.delete(verificationCode);

            logger.info("Password reset successfully for email: {}", email);
        } catch (Exception e) {
            logger.error("Error during password reset for email: {}", email, e);
            throw e;
        }
    }

    @Transactional
    public void resetPasswordNoAuth(ResetPasswordDTO resetPasswordDTO) {
        try {
            String email = resetPasswordDTO.getEmail();
            logger.info("Starting password reset process for email: {}", email);

            // Lấy code từ resetPasswordDTO
            String code = resetPasswordDTO.getCode();

            // Tìm mã xác thực
            VerificationCode verificationCode = verificationCodeRepository.findByEmailAndCode(email, code)
                    .orElseThrow(() -> {
                        logger.error("Invalid or expired verification code for email: {}", email);
                        return new IllegalArgumentException("Invalid or expired verification code");
                    });

            // Kiểm tra xem mã xác thực đã hết hạn chưa
            if (verificationCode.getExpiryDate().isBefore(LocalDateTime.now())) {
                logger.error("Verification code expired for email: {}", email);
                throw new IllegalArgumentException("Verification code expired");
            }

            // Tìm người dùng
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        logger.error("User not found for email: {}", email);
                        return new UsernameNotFoundException("User not found");
                    });

            // Kiểm tra xem mật khẩu mới có khớp nhau không
            if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmNewPassword())) {
                logger.error("New passwords do not match for email: {}", email);
                throw new IllegalArgumentException("New passwords do not match");
            }

            // Cập nhật mật khẩu mới cho người dùng
            user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
            userRepository.save(user);

            // Xóa mã xác thực sau khi sử dụng
            verificationCodeRepository.delete(verificationCode);

            logger.info("Password reset successfully for email: {}", email);
        } catch (Exception e) {
            logger.error("Error during password reset for email: {}", resetPasswordDTO.getEmail(), e);
            throw e;
        }
    }


    @Override
    public void sendForgotPasswordCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String code = generateVerificationCode();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setExpiryDate(LocalDateTime.now().plusMinutes(5));  // Mã hết hạn sau 5 phút
        verificationCodeRepository.save(verificationCode);

        // Tạo context cho template
        Context context = new Context();
        context.setVariable("code", code);

        // Gửi email xác nhận với template HTML
        emailService.sendHtmlMessage(email, "Your verification code", "verificationTemplate", context);

        logger.info("Verification code sent to email: {}", email);
    }

    private String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

}
