
package com.egallery.model.entity;

import com.egallery.model.dto.ArtworkDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Artwork extends BaseEntity {
    private String title;
    private String slug;
    private String imageUrl;
    private String description;
    private BigDecimal price;
    private String dimensions;
    private String visibility;

    // In Artwork.java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
//    @JsonIgnore // this alone will solve it for now
    private ApplicationUser artist;


    @ManyToOne
    private ArtType artType;

    @ManyToMany(mappedBy = "artworks")
    private Set<Exhibition> exhibitions = new HashSet<>();
    @OneToMany
    @JoinColumn(name = "targetId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "target_type = 'ARTWORK'")
    @JsonIgnore
    private List<PostLike> likes;

    @OneToMany
    @JoinColumn(name = "targetId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "target_type = 'ARTWORK'")
    @OrderBy("createdAt ASC")
    private List<Comment> comments;

    public ArtworkDto toDto(ApplicationUser currentUser) {
        ArtworkDto dto = new ArtworkDto();
        dto.setId(getId());
        dto.setTitle(getTitle());
        dto.setImageUrl(getImageUrl());
        dto.setDescription(getDescription());
        dto.setPrice(getPrice());
        dto.setArtist(getArtist().mapToDto());
        dto.setLikes((long)(likes==null?0:likes.size()));
        dto.setComments((long)(comments==null?0:comments.size()));

        boolean isLiked = false;
        if (currentUser != null && likes != null) {
            isLiked = likes.stream()
                    .anyMatch(pl -> pl.getApplicationUser().getId()
                            .equals(currentUser.getId()));
        }
        dto.setLiked(isLiked);
        return dto;
    }

}
