package com.egallery.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CreateEventRequest {
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String venueName;
    private String venueAddress;
    private String meetingLink;
    private Boolean isPublic;
    private List<String> links;
    private List<UUID> artworkIds; // optional exhibition artworks
    private String bannerImage;
}
