package com.fptaptech.s4.controller;

import com.fptaptech.s4.dto.ResetPasswordDTO;
import com.fptaptech.s4.exception.UserAlreadyExistsException;
import com.fptaptech.s4.entity.User;
import com.fptaptech.s4.repository.TokenRepository;
import com.fptaptech.s4.request.LoginRequest;
import com.fptaptech.s4.response.JwtResponse;
import com.fptaptech.s4.security.jwt.JwtUtils;
import com.fptaptech.s4.security.user.HotelUserDetails;
import com.fptaptech.s4.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final TokenRepository tokenRepository;

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, @RequestParam String role) {
        try {
            userService.registerUser(user, role);
            return ResponseEntity.ok("Registration successful!");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtTokenForUser(authentication);
        HotelUserDetails userDetails = (HotelUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.ok(new JwtResponse(
                userDetails.getId(),
                userDetails.getEmail(),
                jwt,
                roles));
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestParam String email) {
        try {
            userService.sendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending verification code: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO, Principal principal) {
        try {
            userService.resetPassword(principal.getName(), resetPasswordDTO);
            return ResponseEntity.ok("Password reset successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error resetting password: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String jwt = parseJwt(request);
        if (jwt != null) {
            tokenRepository.revokeToken(jwt);
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successful");
    }

    private String parseJwt(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @PostMapping("/send-forgot-password-code")
    public ResponseEntity<?> sendForgotPasswordCode(@RequestParam String email) {
        try {
            userService.sendForgotPasswordCode(email);
            return ResponseEntity.ok("Verification code sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending verification code: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password-no-auth")
    public ResponseEntity<?> resetPasswordNoAuth(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            userService.resetPasswordNoAuth(resetPasswordDTO);
            return ResponseEntity.ok("Password reset successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error resetting password: " + e.getMessage());
        }
    }

}





