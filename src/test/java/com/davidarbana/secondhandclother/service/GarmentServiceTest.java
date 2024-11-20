package com.davidarbana.secondhandclother.service;

import com.davidarbana.secondhandclother.exception.CustomException;
import com.davidarbana.secondhandclother.model.Garment;
import com.davidarbana.secondhandclother.model.GarmentRequest;
import com.davidarbana.secondhandclother.model.User;
import com.davidarbana.secondhandclother.repository.GarmentRepository;
import com.davidarbana.secondhandclother.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class GarmentServiceTest {

    @Mock
    private GarmentRepository garmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GarmentService garmentService;

    private User user;
    private GarmentRequest garmentRequest;
    private Garment garment;
    private Principal principal;

    @BeforeEach
    public void setUp() {
        // Initialize mock data
        user = new User(123L, "fullNameTest","addressTest","usernameTest","passwordTest");
        garmentRequest = new GarmentRequest("Shirt", "T-shirt", 123L, "M", 20.0);
        garment = Garment.builder().id(1L).type("Shirt").description("T-shirt").size("M").price(20.0).user(user).build();
        principal = new UsernamePasswordAuthenticationToken(user, null);
    }

    @Test
    public void testGetGarmentByIdSuccess() throws CustomException {
        // Mock the behavior of the garmentRepository
        when(garmentRepository.findById(1L)).thenReturn(Optional.of(garment));

        // Call the service method
        Garment result = garmentService.getGarmentById(1L);

        // Verify the result
        assertNotNull(result);
        assertEquals(garment.getId(), result.getId());
        assertEquals(garment.getType(), result.getType());
    }

    @Test
    public void testGetGarmentByIdNotFound() {
        // Mock the behavior of the garmentRepository to return an empty Optional
        when(garmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the service method and assert that the exception is thrown
        assertThrows(CustomException.class, () -> garmentService.getGarmentById(1L));
    }

    @Test
    public void testPublishGarmentSuccess() {
        // Mock the userRepository to return the user
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Mock the garmentRepository to return the saved garment
        when(garmentRepository.save(Mockito.any(Garment.class))).thenReturn(garment);

        // Call the service method
        Garment result = garmentService.publishGarment(garmentRequest, principal);

        // Verify the result
        assertNotNull(result);
        assertEquals(garment.getType(), result.getType());
        assertEquals(garment.getPrice(), result.getPrice());

        // Verify that the save method of garmentRepository was called
        verify(garmentRepository).save(Mockito.any(Garment.class));
    }

    @Test
    public void testPublishGarmentInvalidPublisher() {
        // Mock the userRepository to return an empty Optional (invalid publisher)
        assert garmentRequest.publisherId() != null;
        when(userRepository.findById(garmentRequest.publisherId())).thenReturn(Optional.empty());

        // Call the service method and assert that the exception is thrown
        assertThrows(IllegalArgumentException.class, () -> garmentService.publishGarment(garmentRequest, principal));
    }

    @Test
    public void testUpdateGarmentSuccess() throws CustomException {
        // Mock existing garment and user validation
        when(garmentRepository.findById(1L)).thenReturn(Optional.of(garment));
        assert garmentRequest.publisherId() != null;
        when(userRepository.findById(garmentRequest.publisherId())).thenReturn(Optional.of(user));

        // Call the service method to update the garment
        garmentService.updateGarment(1L, garmentRequest, principal);

        // Verify that the save method of garmentRepository is called for update
        verify(garmentRepository).save(Mockito.any(Garment.class));
    }

    @Test
    public void testUpdateGarmentNotOwner() {
        // Set up a different user as the principal
        User differentUser = new User(123L, "fullNameTest","addressTest","usernameTest","passwordTest");
        Principal otherPrincipal = new UsernamePasswordAuthenticationToken(differentUser, null);

        // Mock the existing garment and user validation
        when(garmentRepository.findById(1L)).thenReturn(Optional.of(garment));
        assert garmentRequest.publisherId() != null;
        when(userRepository.findById(garmentRequest.publisherId())).thenReturn(Optional.of(user));

        // Call the service method and assert that the exception is thrown (user is not the owner)
        assertThrows(CustomException.class, () -> garmentService.updateGarment(1L, garmentRequest, otherPrincipal));
    }

    @Test
    public void testDeleteGarmentSuccess() {
        // Mock the garmentRepository to return the garment
        when(garmentRepository.findById(1L)).thenReturn(Optional.of(garment));

        // Call the service method
        garmentService.deleteGarment(1L, principal);

        // Verify that deleteById was called
        verify(garmentRepository).deleteById(1L);
    }

    @Test
    public void testDeleteGarmentNotOwner() {
        // Set up a different user as the principal
        User differentUser = new User(123L, "fullNameTest","addressTest","usernameTest","passwordTest");
        Principal otherPrincipal = new UsernamePasswordAuthenticationToken(differentUser, null);

        // Mock the garmentRepository to return the garment
        when(garmentRepository.findById(1L)).thenReturn(Optional.of(garment));

        // Call the service method and assert that it doesn't delete the garment
        garmentService.deleteGarment(1L, otherPrincipal);

        // Verify that deleteById was not called
        verify(garmentRepository, Mockito.never()).deleteById(1L);
    }
}
