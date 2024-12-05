package com.fptaptech.s4.service.interfaces;

import com.fptaptech.s4.dto.ResetPasswordDTO;
import com.fptaptech.s4.dto.UserDTO;
import com.fptaptech.s4.dto.UserUpdateDTO;
import com.fptaptech.s4.entity.User;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IUserService {
    UserDTO findByEmail(String email);
    User registerUser(User user, String roleName);
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);
    UserDTO findById(Long userId);
    void resetPassword(String email, ResetPasswordDTO resetPasswordDTO);

    @Transactional
    void updateUser(UserUpdateDTO userUpdateDTO, String email);
}
