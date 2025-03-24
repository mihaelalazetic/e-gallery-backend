
package com.egallery.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Venue extends BaseEntity {
    private String name;
    private String address;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
}
