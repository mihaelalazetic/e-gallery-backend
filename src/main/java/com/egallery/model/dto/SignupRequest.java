package com.egallery.model.dto;

import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    private String username;
    private String email;
    private String fullName;
    private String password;
    private String confirmPassword;
    private String role;
    private String bio;
    private String profilePictureUrl;
}
