package com.fptaptech.s4.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fptaptech.s4.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "Invalid email address")
    private String userName;

    @NotBlank(message = "First name cannot be empty")
    @Size(min = 1, max = 1000, message = "First name must be between 1 and 1000 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 1, max = 1000, message = "Last name must be between 1 and 1000 characters")
    private String lastName;

    @NotBlank(message = "Phone cannot be empty")
    @Size(min = 1, max = 10, message = "Phone number must be between 1 and 10 characters")
    private String phone;

    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
    @Pattern(regexp = ".*[!@#$%^&*()].*", message = "Password must contain at least one special character")
    private String password;

    private List<Role> roles = new ArrayList<>();
//    private List<ShuttleBookingDTO> shuttleBookings = new ArrayList<>();
//    private List<SpaBookingDTO> spaBookings = new ArrayList<>();
//    private List<RestaurantDTO> restaurantBookings = new ArrayList<>();

    public String getEmail() {
        return userName;
    }
}
