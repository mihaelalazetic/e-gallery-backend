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
    private String email;
    private Set<String> roles;
}
