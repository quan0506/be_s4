package com.fptaptech.s4.controller;

import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.dto.UserDTO;
import com.fptaptech.s4.dto.UserUpdateDTO;
import com.fptaptech.s4.exception.UserAlreadyExistsException;
import com.fptaptech.s4.entity.User;
import com.fptaptech.s4.service.impl.UserService;
import com.fptaptech.s4.service.interfaces.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @Autowired
    private final UserService userServices;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> getUsers(){
        Response response = userServices.getUsers();
        response.setStatusCode(200);
        response.setMessage("successful");
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, @RequestParam String role) {
        try {
            User registeredUser = userService.registerUser(user, role);
            return ResponseEntity.ok(registeredUser);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user: " + e.getMessage());
        }
    }

    @GetMapping("/me") public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        UserDTO user = userService.getCurrentUser(authentication);
        return ResponseEntity.ok(user); }

    @PutMapping("/update")
    /*@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")*/
    public ResponseEntity<String> updateUser(@RequestBody UserUpdateDTO userUpdateDTO, Authentication authentication) {
        String email = authentication.getName(); // Lấy email của người dùng đăng nhập
        userService.updateUser(userUpdateDTO, email);
        return ResponseEntity.ok("Update Success");
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email){
        try{
            UserDTO user = userService.findByEmail(email);
            return ResponseEntity.ok(user);
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user");
        }
    }

    @DeleteMapping("/delete/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (#email == principal.username and (hasRole('ROLE_USER') or hasRole('ROLE_EMPLOYEE')))")
    public ResponseEntity<String> deleteUser(@PathVariable("email") String email) {
        try {
            userService.deleteUser(email);
            return ResponseEntity.ok("User deleted successfully");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user: " + e.getMessage());
        }
    }

}
