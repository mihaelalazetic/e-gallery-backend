package com.egallery.model.dto;

import com.egallery.model.entity.Artwork;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtworkDto {
    private BigDecimal price;
    private UUID id;
    private String title;
    private String imageUrl;
    private String description;
    private ApplicationUserDTO artist;
    private Long likes;
    private Long comments;
    private boolean liked;

}
