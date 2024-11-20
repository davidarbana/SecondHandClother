
package com.davidarbana.secondhandclother.model;

import jakarta.annotation.Nullable;
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
    @Nullable private String type;
    @Nullable private String description;
    @Nullable private String size;
    @Nullable private double price;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
