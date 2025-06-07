package com.egallery.model.dto;

import com.egallery.model.entity.Event;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDto {
    private String name;
    private String description;
    private String location;
    private String startDate;
    private String endDate;
    private boolean isPublic;
    private String bannerImageUrl;
    private String shareableLink;
    private String createdBy;
    private String type; // Enum: EXHIBITION, MEETUP, etc.
    private String[] tags;
    private String[] artworkIds;
    private String slug;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static EventDto from(Event e) {
        EventDto dto = EventDto.builder()
                .name(e.getTitle())
                .description(e.getDescription())
                // if you want address+city+country, concatenate however you like:
                .location(e.getVenue().getName())
                .startDate(e.getStartDate().format(FORMATTER))
                .endDate(e.getEndDate().format(FORMATTER))
                .isPublic(Boolean.TRUE.equals(e.getIsPublic()))
                .bannerImageUrl(e.getBannerImageUrl())
                .shareableLink(e.getShareableLink())
                .createdBy(e.getCreatedBy().getUsername())
                .type(e.getType().name())
                .tags(e.getTags().toArray(new String[0]))
                .artworkIds(
                        e.getArtworks().stream()
                                .map(a -> a.getId().toString())
                                .toArray(String[]::new)
                )
                .slug(e.getSlug())
                .build();
        return dto;
    }
}
