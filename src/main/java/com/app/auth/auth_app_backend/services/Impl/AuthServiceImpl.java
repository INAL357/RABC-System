package com.app.auth.auth_app_backend.services.Impl;

import com.app.auth.auth_app_backend.dto.UserDto;
import com.app.auth.auth_app_backend.entities.User;
import com.app.auth.auth_app_backend.services.AuthService;
import com.app.auth.auth_app_backend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    @Override
    public UserDto registerUser(UserDto userDto) {


       UserDto userDto1= userService.createUser(userDto);
       return userDto1;
    }
}
