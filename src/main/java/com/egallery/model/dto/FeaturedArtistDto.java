package com.egallery.model.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeaturedArtistDto {
    private UUID id;
    private String username;
    private String avatarUrl;
    private long totalLikes;

    // getters
}
