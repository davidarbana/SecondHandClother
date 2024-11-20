package com.davidarbana.secondhandclother.service;

import com.davidarbana.secondhandclother.model.Garment;
import com.davidarbana.secondhandclother.model.User;
import com.davidarbana.secondhandclother.repository.GarmentRepository;
import com.davidarbana.secondhandclother.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GarmentService {

    private final GarmentRepository garmentRepository;
    private final UserRepository userRepository;

    // Get all garments with optional type filter
    public List<Garment> getAllGarments(String type) {
        if (type != null) {
            return garmentRepository.findByTypeContainingIgnoreCase(type);
        }
        return garmentRepository.findAll();
    }

    // Get a garment by its ID
    public Garment getGarmentById(Long id) {
        Optional<Garment> garment = garmentRepository.findById(id);
        return garment.orElse(null);
    }

    // Publish a new garment
    public void publishGarment(Garment garment) {

//        Optional<User> user = userRepository.findById(152L);
//        if (user.isEmpty()) {
//            throw new IllegalArgumentException("Wrong user id");
//        }
//        Garment vroomVroom = Garment.builder().publisherId("152").description("Vroom vroom").build();
        garmentRepository.save(garment);
    }

    // Update a garment
    public boolean updateGarment(Long id, String userId, Garment garment) {
        Optional<Garment> existingGarment = garmentRepository.findById(id);
        if (existingGarment.isPresent() && existingGarment.get().getPublisherId().equals(userId)) {
            garment.setId(id);
            garmentRepository.save(garment);
            return true;
        }
        return false;
    }

    // Delete a garment
    public boolean deleteGarment(Long id, String userId) {
        Optional<Garment> existingGarment = garmentRepository.findById(id);
        if (existingGarment.isPresent() && existingGarment.get().getPublisherId().equals(userId)) {
            garmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
