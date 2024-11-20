package com.davidarbana.secondhandclother.controller;

import com.davidarbana.secondhandclother.model.Garment;
import com.davidarbana.secondhandclother.repository.GarmentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clothes")
public class GarmentController {

    private final GarmentRepository garmentRepository;

    public GarmentController (GarmentRepository garmentRepository) {
        this.garmentRepository = garmentRepository;
    }

    /**
     * List garments with optional filters.
     *
     * @param type     the type of garment (optional)
     * @param size     the size of garment (optional)
     * @param minPrice the minimum price range (optional)
     * @param maxPrice the maximum price range (optional)
     * @return a list of garments matching the filters
     */
    @Operation(summary = "List garments", description = "Fetch a list of garments with optional filters for type, size, and price range.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of garments",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Garment.class)))
    })
    @GetMapping
    public List<Garment> listGarments(
            @Parameter(description = "Filter garments by type") @RequestParam(required = false) String type,
            @Parameter(description = "Filter garments by size") @RequestParam(required = false) String size,
            @Parameter(description = "Filter garments by minimum price") @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Filter garments by maximum price") @RequestParam(required = false) Double maxPrice) {
        if (type != null) {
            return garmentRepository.findByTypeContainingIgnoreCase(type);
        }
        if (size != null) {
            return garmentRepository.findBySizeContainingIgnoreCase(size);
        }
        if (minPrice != null && maxPrice != null) {
            return garmentRepository.findByPriceBetween(minPrice, maxPrice);
        }
        return garmentRepository.findAll();
    }

    /**
     * Get garment details by ID.
     *
     * @param id the ID of the garment
     * @return the details of the garment
     */
    @Operation(summary = "Get garment details", description = "Fetch details of a garment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved garment details",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Garment.class))),
            @ApiResponse(responseCode = "404", description = "Garment not found",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public Garment getGarment(
            @Parameter(description = "ID of the garment to retrieve", required = true)
            @PathVariable String id) {
        return garmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Garment not found"));
    }
}
