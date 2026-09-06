package com.app.auth.auth_app_backend.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        String mesage,
        HttpStatus status,
        int error

) {



}
