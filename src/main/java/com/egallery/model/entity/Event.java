
package com.egallery.model.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private String meetingLink;
    private Boolean isPublic;
    private String shareableLink;

    @ManyToOne
    private ApplicationUser createdBy;

    @ManyToOne
    private Venue venue;

    @OneToOne
    private Exhibition exhibition;
}
