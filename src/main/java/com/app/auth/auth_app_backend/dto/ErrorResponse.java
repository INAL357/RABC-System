package com.app.auth.auth_app_backend.dto;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

public record ErrorResponse(
        String mesage,
        HttpStatus status,
        String error

) {



}
