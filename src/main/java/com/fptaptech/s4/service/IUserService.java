package com.fptaptech.s4.service;

import com.fptaptech.s4.entity.User;

import java.util.List;

public interface IUserService {
    User registerUser(User user, String roleName);
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);

}
