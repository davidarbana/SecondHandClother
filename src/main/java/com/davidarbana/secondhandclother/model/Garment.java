
package com.davidarbana.secondhandclother.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Garment {
    @Id
    private String id;
    //Can be an ENUM
    private String type;
    private String description;
    private String publisherId;
    private String size;
    private double price;
}
