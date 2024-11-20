package com.davidarbana.secondhandclother.service;

import com.davidarbana.secondhandclother.model.Garment;
import com.davidarbana.secondhandclother.repository.GarmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GarmentService {

    @Autowired
    private GarmentRepository garmentRepository;

    // Get all garments with optional type filter
    public List<Garment> getAllGarments(String type) {
        if (type != null) {
            return garmentRepository.findByTypeContainingIgnoreCase(type);
        }
        return garmentRepository.findAll();
    }

    // Get a garment by its ID
    public Garment getGarmentById(String id) {
        Optional<Garment> garment = garmentRepository.findById(id);
        return garment.orElse(null);
    }

    // Publish a new garment
    public void publishGarment(Garment garment) {
        garmentRepository.save(garment);
    }

    // Update a garment
    public boolean updateGarment(String id, String userId, Garment garment) {
        Optional<Garment> existingGarment = garmentRepository.findById(id);
        if (existingGarment.isPresent() && existingGarment.get().getPublisherId().equals(userId)) {
            garment.setId(id);
            garmentRepository.save(garment);
            return true;
        }
        return false;
    }

    // Delete a garment
    public boolean deleteGarment(String id, String userId) {
        Optional<Garment> existingGarment = garmentRepository.findById(id);
        if (existingGarment.isPresent() && existingGarment.get().getPublisherId().equals(userId)) {
            garmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
