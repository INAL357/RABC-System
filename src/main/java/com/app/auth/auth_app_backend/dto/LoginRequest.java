package com.app.auth.auth_app_backend.dto;

public record LoginRequest(
        String email,
        String password
) {

}
