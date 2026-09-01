package com.app.auth.auth_app_backend.services;

import com.app.auth.auth_app_backend.dto.UserDto;

public interface AuthService {

    public UserDto registerUser(UserDto userDto);
}
