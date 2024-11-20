package com.davidarbana.secondhandclother.repository;

import com.davidarbana.secondhandclother.model.Garment;
import com.davidarbana.secondhandclother.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository
public interface GarmentRepository extends JpaRepository<Garment, String> {
    // Find garments by type (case-insensitive search)
    List<Garment> findByTypeContainingIgnoreCase(String type);

    // Find garments by size (case-insensitive search)
    List<Garment> findBySizeContainingIgnoreCase(String size);

    // Find garments within a price range
    List<Garment> findByPriceBetween(double minPrice, double maxPrice);

    List<Garment> findAll();

    Optional<Garment> findById(String id);

}
