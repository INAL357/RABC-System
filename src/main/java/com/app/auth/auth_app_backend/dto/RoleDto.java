package com.app.auth.auth_app_backend.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Setter
@Getter
@NoArgsConstructor
public class RoleDto {

    private UUID id;
    private String roleType;
}
