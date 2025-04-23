package com.egallery.model.dto;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUserDTO {
    private UUID id;
    private String username;
    private String fullName;
    private String email;
    private Set<String> roles;
    private String profilePictureUrl;
    private String bio;
    private Long totalLikes;
}
