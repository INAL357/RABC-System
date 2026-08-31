package com.app.auth.auth_app_backend.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "entity")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;
}
