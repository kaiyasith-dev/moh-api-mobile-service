package apb.co.la.moh.api.mobile.service.controller;

import apb.co.la.moh.api.mobile.service.client.ApiServiceClient;
import apb.co.la.moh.api.mobile.service.dto.ApiResponse;
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
@Tag(name = "MOH Hospital Location")
public class LocationController {

    private final ApiServiceClient integrationServiceClient;

    @GetMapping
    @Operation(summary = "Get all locations")
    public ResponseEntity<MobileResponse<MobileResponse.BodyWithMeta<Object, Object>>> getAllLocations(
            @Parameter(description = "Page number (starts from 1)")
            @RequestParam(defaultValue = "1") int page,

            @Parameter(description = "Number of items per page")
            @RequestParam(defaultValue = "10") int size) {

        log.info("GET /api/v1/mobile/locations - Fetching all locations via integration service");

        ApiResponse<Object> apiResponse =
                integrationServiceClient.getAllLocations(page, size);

        Object data = apiResponse != null ? apiResponse.getData() : null;
        Object meta = apiResponse != null ? apiResponse.getMeta() : null;

        return ResponseEntity.ok(
                MobileResponse.success(data, meta)
        );
    }

    @GetMapping("/nearest")
    @Operation(summary = "Find nearest hospitals based on GPS coordinates")
    public ResponseEntity<MobileResponse<Object>> findNearestHospitals(
            @Parameter(description = "Latitude", example = "18.0245345") @RequestParam Double latitude,
            @Parameter(description = "Longitude", example = "102.653008") @RequestParam Double longitude,
            @Parameter(description = "Radius in km", example = "5.000") @RequestParam Double radiusKm) {
        log.info("GET /api/v1/mobile/locations/nearest - Finding nearest hospitals via integration service");
        ApiResponse<Object> apiResponse = integrationServiceClient.findNearestHospitals(latitude, longitude, radiusKm);
        return ResponseEntity.ok(MobileResponse.success(apiResponse != null ? apiResponse.getData() : null));
    }

    @GetMapping("/search")
    @Operation(summary = "Search for locations by keyword")
    public ResponseEntity<MobileResponse<Object>> searchLocations(
            @Parameter(description = "Search keyword") @RequestParam(required = false) String keyword,
            @Parameter(description = "Page number (starts from 1)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/v1/mobile/locations/search - Search locations via integration service");
        ApiResponse<Object> apiResponse = integrationServiceClient.searchLocations(keyword, page, size);
        return ResponseEntity.ok(MobileResponse.success(apiResponse != null ? apiResponse.getData() : null));
    }

//    @GetMapping("/parent/{parentId}")
//    @Operation(summary = "Get child locations by Parent ID")
//    public ResponseEntity<MobileResponse<Object>> getLocationsByParentId(
//            @Parameter(description = "Parent location ID") @PathVariable String parentId) {
//        log.info("GET /api/v1/mobile/locations/parent/{} - Fetching child locations via integration service", parentId);
//        ApiResponse<Object> apiResponse = integrationServiceClient.getLocationsByParentId(parentId);
//        return ResponseEntity.ok(MobileResponse.success(apiResponse != null ? apiResponse.getData() : null));
//    }

    @GetMapping("/{id}")
    @Operation(summary = "Get specific location by ID")
    public ResponseEntity<MobileResponse<Object>> getLocationById(
            @Parameter(description = "Location ID") @PathVariable String id) {
        log.info("GET /api/v1/mobile/locations/{} - Fetching location details via integration service", id);
        ApiResponse<Object> apiResponse = integrationServiceClient.getLocationById(id);
        return ResponseEntity.ok(MobileResponse.success(apiResponse != null ? apiResponse.getData() : null));
    }
}
