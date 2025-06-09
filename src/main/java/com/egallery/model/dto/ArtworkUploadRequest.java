package com.egallery.model.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ArtworkUploadRequest {
    private String title;
    private String description;
    private List<String> imageUrl;
    private BigDecimal price;
    private String dimensions;
    private String visibility;
    private List<Long> categoryIds; // selected from dropdown on frontend
    private List<String> relevantLinks;
}
