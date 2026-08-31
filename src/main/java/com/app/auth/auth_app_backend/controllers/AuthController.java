package com.app.auth.auth_app_backend.controllers;

import com.app.auth.auth_app_backend.dto.UserDto;
import com.app.auth.auth_app_backend.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/c1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<UserDto> RegisterUser(@RequestBody  UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.RegisterUser(userDto));
    }
}
