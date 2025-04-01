package com.egallery.model.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ArtworkUploadRequest {
    private String title;
    private String imageUrl;
    private BigDecimal price;
    private String dimensions;
    private String visibility;
    private Long artTypeId; // selected from dropdown on frontend
}
