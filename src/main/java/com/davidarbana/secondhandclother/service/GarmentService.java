package com.davidarbana.secondhandclother.service;

import com.davidarbana.secondhandclother.exception.CustomException;
import com.davidarbana.secondhandclother.model.Garment;
import com.davidarbana.secondhandclother.model.GarmentRequest;
import com.davidarbana.secondhandclother.model.User;
import com.davidarbana.secondhandclother.repository.GarmentRepository;
import com.davidarbana.secondhandclother.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GarmentService {

    private final GarmentRepository garmentRepository;
    private final UserRepository userRepository;

    private static void assertValidPublisherId(UsernamePasswordAuthenticationToken principal, Optional<User> publisherUser) {
        if (publisherUser.isEmpty()) {
            throw new IllegalArgumentException("Invalid publisher id");
        }
        User userFromToken = (User) principal.getPrincipal();
        if (!userFromToken.getId().equals(publisherUser.get().getId())) {
            throw new IllegalArgumentException("Can not add garments for other publishers other than your own.");
        }
    }


    // Get a garment by its ID
    public Garment getGarmentById(Long id) throws CustomException {
        Optional<Garment> garment = garmentRepository.findById(id);
        if (garment.isEmpty()) {
            throw new CustomException("No Garments found");
        }
        return garment.orElse(null);
    }

    // Publish a new garment
    public Garment publishGarment(GarmentRequest garmentRequest, Principal principal) {
        assert garmentRequest.publisherId() != null;
        Optional<User> publisherUser = userRepository.findById(garmentRequest.publisherId());
        assertValidPublisherId((UsernamePasswordAuthenticationToken) principal, publisherUser);
        Garment garment = Garment.builder().type(garmentRequest.type()).description(garmentRequest.description()).size(garmentRequest.size()).price(garmentRequest.price()).user(publisherUser.get()).build();

//        Optional<User> user = userRepository.findById(152L);
//        if (user.isEmpty()) {
//            throw new IllegalArgumentException("Wrong user id");
//        }
//        Garment vroomVroom = Garment.builder().publisherId("152").description("Vroom vroom").build();
        return garmentRepository.save(garment);
    }

    // Update a garment
    public void updateGarment(Long id, GarmentRequest garmentRequest, Principal principal) throws CustomException {
        assert garmentRequest.publisherId() != null;
        Optional<User> publisherUser = userRepository.findById(garmentRequest.publisherId());
        assertValidPublisherId((UsernamePasswordAuthenticationToken) principal, publisherUser);
        Optional<Garment> existingGarment = garmentRepository.findById(id);
        if (existingGarment.isPresent()) {
            Long principalId = ((User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getId();
            if (!existingGarment.get().getUser().getId().equals(principalId)) {
                throw new CustomException("The garment does not belong to this user.");
            }
            Garment updatedGarment = Garment.builder()
                    .id(existingGarment.get().getId())
                    .user(existingGarment.get().getUser())
                    .price(garmentRequest.price())
                    .type(garmentRequest.type())
                    .description(garmentRequest.description())
                    .size(garmentRequest.size())
                    .build();
            garmentRepository.save(updatedGarment);
        }
    }

    // Delete a garment
    public void deleteGarment(Long id, Principal principal) {
        Optional<Garment> existingGarment = garmentRepository.findById(id);
        Long principalId = ((User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getId();
        if (existingGarment.isPresent() && existingGarment.get().getUser().getId().equals(principalId)) {
            garmentRepository.deleteById(id);
        }
    }

    public List<Garment> listGarments(String type, String size, Double minPrice, Double maxPrice) {
        Specification<Garment> spec = Specification.where(GarmentRepository.hasType(type))
                .or(GarmentRepository.hasSize(size))
                .or(GarmentRepository.hasPriceBetween(minPrice, maxPrice));

        return garmentRepository.findAll(spec);
    }
}
