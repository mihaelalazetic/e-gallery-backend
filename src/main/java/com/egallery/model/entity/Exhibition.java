
package com.egallery.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exhibition extends BaseEntity {
    private String title;
    private String slug;
    private String description;
    private String bannerImageUrl;
    private Boolean isOnline;
    private String curatorNotes;
    private String shareableLink;

    @ManyToOne
    private ApplicationUser createdBy;

    @ManyToOne
    private Venue venue;

    @OneToOne
    private Event event;

    @ManyToMany
    @JoinTable(
        name = "artwork_exhibition",
        joinColumns = @JoinColumn(name = "exhibition_id"),
        inverseJoinColumns = @JoinColumn(name = "artwork_id")
    )
    private Set<Artwork> artworks = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "exhibition_featured_artists",
        joinColumns = @JoinColumn(name = "exhibition_id"),
        inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private Set<ApplicationUser> featuredArtists = new HashSet<>();
}
