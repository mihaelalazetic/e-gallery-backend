
package com.egallery.model.entity;

import com.egallery.model.enums.EventType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event extends BaseEntity {
    private String title;
    private String slug;
    private String description;
    private String bannerImageUrl;
    private Boolean isPublic;
    private String shareableLink;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", shape = JsonFormat.Shape.STRING)
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", shape = JsonFormat.Shape.STRING)
    private LocalDateTime endDate;

    @ManyToOne
    private ApplicationUser createdBy;

    @ManyToOne
    @JsonBackReference
    private Venue venue;

    @ManyToMany
    @JoinTable(
            name = "event_artworks",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "artwork_id")
    )
    @JsonManagedReference
    private Set<Artwork> artworks = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private EventType type; // Enum: EXHIBITION, MEETUP, etc.

    @ElementCollection
    @CollectionTable(name = "event_tag_strings", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();


    @ManyToMany
    @JoinTable(
            name = "event_featured_artists",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private Set<ApplicationUser> featuredArtists = new HashSet<>();
//    @OneToOne
//    private Exhibition exhibition;
}
