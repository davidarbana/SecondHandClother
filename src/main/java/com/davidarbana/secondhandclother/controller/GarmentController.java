package com.davidarbana.secondhandclother.controller;

import com.davidarbana.secondhandclother.exception.CustomException;
import com.davidarbana.secondhandclother.model.Garment;
import com.davidarbana.secondhandclother.model.GarmentRequest;
import com.davidarbana.secondhandclother.service.GarmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/clothes")
public class GarmentController {

    private final GarmentService garmentService;

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
    public ResponseEntity<List<Garment>> listGarments(
            @Parameter(description = "Filter garments by type") @RequestParam(required = false) String type,
            @Parameter(description = "Filter garments by size") @RequestParam(required = false) String size,
            @Parameter(description = "Filter garments by minimum price") @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Filter garments by maximum price") @RequestParam(required = false) Double maxPrice) {


//        if (type != null) {
//            response = garmentRepository.findByTypeContainingIgnoreCase(type);
//        }
//        if (size != null) {
//            response = garmentRepository.findBySizeContainingIgnoreCase(size);
//        }
//        if (minPrice != null && maxPrice != null) {
//            response = garmentRepository.findByPriceBetween(minPrice, maxPrice);
//        } else if(type==null && size==null && minPrice==null && maxPrice==null){
//            response = garmentRepository.findAll();
//        }
        List<Garment> response = garmentService.listGarments(type, size, minPrice, maxPrice);

        return ResponseEntity.ok(response);
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
    public ResponseEntity<Garment> getGarment(
            @Parameter(description = "ID of the garment to retrieve", required = true)
            @PathVariable Long id) throws CustomException {
        return ResponseEntity.ok(garmentService.getGarmentById(id));
    }

    /**
     * Create a new garment.
     *
     * @param garment    the garment data to create
     * @param principal  the authenticated user performing the operation
     */
    @Operation(summary = "Create a new garment", description = "Create a new garment and add it to the collection.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new garment")
    })
    @PostMapping
    public ResponseEntity<Garment> createGarment(@RequestBody GarmentRequest garment, Principal principal) {

        return ResponseEntity.ok(garmentService.publishGarment(garment, principal));
    }

    /**
     * Update an existing garment.
     *
     * @param id             the ID of the garment to update
     * @param garmentRequest the new garment data
     * @param principal      the authenticated user performing the operation
     * @return
     */
    @Operation(summary = "Update a garment", description = "Update the details of an existing garment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the garment"),
            @ApiResponse(responseCode = "404", description = "Garment not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> patchGarment(@PathVariable("id") Long id, @RequestBody GarmentRequest garmentRequest, Principal principal) throws CustomException {
        garmentService.updateGarment(id, garmentRequest, principal);
        return ResponseEntity.ok("Garment Updated");
    }

    /**
     * Delete an existing garment.
     *
     * @param id        the ID of the garment to delete
     * @param principal the authenticated user performing the operation
     */
    @Operation(summary = "Delete a garment", description = "Delete an existing garment from the collection.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the garment"),
            @ApiResponse(responseCode = "404", description = "Garment not found",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGarment(@PathVariable("id") Long id, Principal principal) {
        garmentService.deleteGarment(id, principal);

        return ResponseEntity.ok("Garment Deleted");
    }
}
