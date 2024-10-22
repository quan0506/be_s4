package com.fptaptech.s4.dto;

import com.fptaptech.s4.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    private Long id;

    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "Invalid email address")
    private String username;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 1, max =1000, message = "Name must only contain letters and numbers")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 1, max =1000, message = "Last name must only contain letters and numbers")
    private String lastName;

    @NotBlank(message = "Phone cannot be empty")
    @Size(min = 1, max = 10, message = "Phone number must lest 1 to 10 letter characters")
    private String phone;
    private Role role;

}
