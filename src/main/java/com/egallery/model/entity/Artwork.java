
package com.egallery.model.entity;

import com.egallery.model.dto.ArtworkDto;
import com.egallery.security.SecurityUtils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Artwork extends BaseEntity {
    private String title;
    private String slug;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "artwork_id")
    private Set<ArtworkImage> images = new HashSet<>();

    private String description;
    private BigDecimal price;
    private String dimensions;
    private String visibility;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "artwork_categories",
            joinColumns = @JoinColumn(name = "artwork_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();


    // In Artwork.java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    @JsonIgnore
//    @JsonIgnore // this alone will solve it for now
    private ApplicationUser artist;


    @ManyToMany(mappedBy = "artworks")
    @JsonBackReference
    private Set<Event> events = new HashSet<>();

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
        dto.setImageUrls(images.stream()
                .sorted(Comparator.comparing(ArtworkImage::getPosition))
                .map(ArtworkImage::getImageUrl)
                .collect(Collectors.toList()));

        dto.setDescription(getDescription());
        dto.setPrice(getPrice());
        dto.setArtist(getArtist().mapToDto());
        dto.setLikes((long) (likes == null ? 0 : likes.size()));
        dto.setCommentCount((long) (comments == null ? 0 : comments.size()));
        dto.setCategories(categories.stream().map(Category::getName).collect(Collectors.toList()));
        dto.setCreatedAt(getCreatedAt());
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
