package com.app.auth.auth_app_backend.services;

import com.app.auth.auth_app_backend.dto.UserDto;

import java.util.Iterator;

public interface UserService {

    //Create User
    UserDto createUser(UserDto userDto);

    //Find by email
    UserDto FindByEmail(String email);

    //Update user
    UserDto UpdateUser(UserDto userDto, String userId);

    //Delete user
    void deleteUser(String userId);

    //Search user
    Iterable<UserDto> getAllUsers();

    //Get User ById
    public UserDto getUserById(String UserId);



}
