
package com.davidarbana.secondhandclother.model;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //Can be an ENUM
    private String type;
    private String description;
    private String publisherId;
    private String size;
    private double price;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
