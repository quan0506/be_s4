package com.fptaptech.s4.controller;

import com.fptaptech.s4.dto.UserDTO;
import com.fptaptech.s4.exception.UserAlreadyExistsException;
import com.fptaptech.s4.entity.User;
import com.fptaptech.s4.service.interfaces.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.FOUND);
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

    @PutMapping("/update")
    public ResponseEntity<Void> updateUser(@RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO);
        return ResponseEntity.noContent().build();
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

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') or hasRole('ROLE_EMPLOYEE') and #email == principal.username)")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String email){
        try{
            userService.deleteUser(email);
            return ResponseEntity.ok("User deleted successfully");
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user: " + e.getMessage());
        }
    }
}
