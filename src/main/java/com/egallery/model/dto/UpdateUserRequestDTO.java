package com.egallery.model.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDTO {
    private UUID id;
    private String email;
    private String bio;
    private String fullName;
    private String profilePictureUrl;

}