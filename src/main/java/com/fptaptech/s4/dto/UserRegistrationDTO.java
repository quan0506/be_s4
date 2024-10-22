package com.fptaptech.s4.dto;

import com.fptaptech.s4.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationDTO {

    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "Invalid email address")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 20, message = "Password must be between 6 to 20 characters")
    private String password;

    @NotBlank(message = "FirstName cannot be empty")
    @Size(min = 1, max =1000, message = "Name must only contain letters and numbers")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 1, max =1000, message = "Last name must only contain letters and numbers")
    private String lastName;

    @NotBlank(message = "Phone cannot be empty")
    @Size(min = 1, max = 10, message = "Phone number must be lest 1 to 10 letter characters")
    private String phone;

    private Role role;

}