package com.egallery.model.entity;

import com.egallery.model.dto.EventDto;
import com.egallery.model.enums.EventType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
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
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public EventDto toDto() {
        return EventDto.builder()
                .name(this.title)
                .description(this.description)
                .location(this.venue != null ? this.venue.getName() : null)
                .locationAddress(this.venue != null ? this.venue.getAddress() : null)
                .startDate(this.startDate != null ? this.startDate.format(FORMATTER) : null)
                .endDate(this.endDate != null ? this.endDate.format(FORMATTER) : null)
                .isPublic(Boolean.TRUE.equals(this.isPublic))
                .bannerImageUrl(this.bannerImageUrl)
                .shareableLink(this.shareableLink)
                .createdBy(this.createdBy != null ? this.createdBy.getUsername() : null)
                .type(this.type != null ? this.type.name() : null)
                .tags(this.tags.toArray(new String[0]))
                .artworkIds(
                        this.artworks.stream()
                                .map(artwork -> artwork.getId().toString())
                                .toArray(String[]::new)
                )
                .slug(this.slug)
                .build();
    }
}