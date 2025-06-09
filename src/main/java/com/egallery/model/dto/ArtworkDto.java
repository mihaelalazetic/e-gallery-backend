package com.egallery.model.dto;

import com.egallery.model.entity.ArtworkLink;
import com.egallery.model.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtworkDto {
    private BigDecimal price;
    private UUID id;
    private String title;
    private List<String> imageUrls;
    private String description;
    private ApplicationUserDTO artist;
    private List<String> categories;
    private List<ArtworkLink> links;
    private Long likes;
    private List<Comment> comments;
    private Long commentCount;
    private boolean liked;
    private LocalDateTime createdAt;

}
