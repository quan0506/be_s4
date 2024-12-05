package com.fptaptech.s4.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDTO {
    private String userName;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;

}
