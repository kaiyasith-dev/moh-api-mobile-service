package apb.co.la.moh.api.mobile.service.controller;

import apb.co.la.moh.api.mobile.service.client.IntegrationServiceClient;
import apb.co.la.moh.api.mobile.service.dto.IntegrationResponse;
import apb.co.la.moh.api.mobile.service.dto.MobileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/mobile/locations")
@RequiredArgsConstructor
@Tag(name = "Mobile Location", description = "Mobile endpoints for accessing locations")
public class LocationController {

    private final IntegrationServiceClient integrationServiceClient;

    @GetMapping
    @Operation(summary = "Get all locations")
    public ResponseEntity<MobileResponse<Object>> getAllLocations(
            @Parameter(description = "Page number (starts from 1)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/v1/mobile/locations - Fetching all locations via integration service");
        IntegrationResponse<Object> integrationResponse = integrationServiceClient.getAllLocations(page, size);
        return ResponseEntity.ok(MobileResponse.success(integrationResponse != null ? integrationResponse.getData() : null));
    }

    @GetMapping("/nearest")
    @Operation(summary = "Find nearest hospitals based on GPS coordinates")
    public ResponseEntity<MobileResponse<Object>> findNearestHospitals(
            @Parameter(description = "Latitude", example = "18.0245345") @RequestParam Double latitude,
            @Parameter(description = "Longitude", example = "102.653008") @RequestParam Double longitude,
            @Parameter(description = "Radius in km", example = "5.000") @RequestParam Double radiusKm) {
        log.info("GET /api/v1/mobile/locations/nearest - Finding nearest hospitals via integration service");
        IntegrationResponse<Object> integrationResponse = integrationServiceClient.findNearestHospitals(latitude, longitude, radiusKm);
        return ResponseEntity.ok(MobileResponse.success(integrationResponse != null ? integrationResponse.getData() : null));
    }

    @GetMapping("/search")
    @Operation(summary = "Search for locations by keyword")
    public ResponseEntity<MobileResponse<Object>> searchLocations(
            @Parameter(description = "Search keyword") @RequestParam(required = false) String keyword,
            @Parameter(description = "Page number (starts from 1)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/v1/mobile/locations/search - Search locations via integration service");
        IntegrationResponse<Object> integrationResponse = integrationServiceClient.searchLocations(keyword, page, size);
        return ResponseEntity.ok(MobileResponse.success(integrationResponse != null ? integrationResponse.getData() : null));
    }

    @GetMapping("/parent/{parentId}")
    @Operation(summary = "Get child locations by Parent ID")
    public ResponseEntity<MobileResponse<Object>> getLocationsByParentId(
            @Parameter(description = "Parent location ID") @PathVariable String parentId) {
        log.info("GET /api/v1/mobile/locations/parent/{} - Fetching child locations via integration service", parentId);
        IntegrationResponse<Object> integrationResponse = integrationServiceClient.getLocationsByParentId(parentId);
        return ResponseEntity.ok(MobileResponse.success(integrationResponse != null ? integrationResponse.getData() : null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get specific location by ID")
    public ResponseEntity<MobileResponse<Object>> getLocationById(
            @Parameter(description = "Location ID") @PathVariable String id) {
        log.info("GET /api/v1/mobile/locations/{} - Fetching location details via integration service", id);
        IntegrationResponse<Object> integrationResponse = integrationServiceClient.getLocationById(id);
        return ResponseEntity.ok(MobileResponse.success(integrationResponse != null ? integrationResponse.getData() : null));
    }
}
