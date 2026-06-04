package apb.co.la.moh.api.mobile.service.controller;

import apb.co.la.moh.api.mobile.service.client.ApiServiceClient;
import apb.co.la.moh.api.mobile.service.dto.ApiResponse;
import apb.co.la.moh.api.mobile.service.dto.MobileResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/mobile/locations")
@RequiredArgsConstructor
@Tag(name = "MOH Hospital Location")
@SecurityRequirement(name = "X-Signature")
@SecurityRequirement(name = "X-Timestamp")
public class LocationController {

    private final ApiServiceClient integrationServiceClient;

    @Data
    public static class PaginationReq {
        @Schema(description = "Page number (starts from 1)", example = "1")
        private int page = 1;
        @Schema(description = "Number of items per page", example = "10")
        private int size = 10;
    }

    @Data
    public static class NearestReq {
        @Schema(description = "Latitude of the user's location", example = "18.0245345")
        private Double latitude;
        @Schema(description = "Longitude of the user's location", example = "102.653008")
        private Double longitude;
        @Schema(description = "Search radius in kilometers", example = "5.000")
        private Double radiusKm;
    }

    @Data
    public static class SearchReq {
        @Schema(description = "Search keyword for hospital name (English or Lao)", example = "ມະໂຫສົດ")
        private String keyword;
        @Schema(description = "Page number (starts from 1)", example = "1")
        private int page = 1;
        @Schema(description = "Number of items per page", example = "10")
        private int size = 10;
    }

    @Data
    public static class DetailReq {
        @Schema(description = "The unique ID of the organization unit/hospital", example = "FV43JisquSm")
        private String id;
    }

    @PostMapping
    public ResponseEntity<MobileResponse<MobileResponse.BodyWithMeta<Object, Object>>> getAllLocations(@RequestBody PaginationReq request) {
        log.info("POST /api/v1/mobile/locations - Fetching all locations via integration service");
        ApiResponse<Object> apiResponse = integrationServiceClient.getAllLocations(request);
        Object data = apiResponse != null ? apiResponse.getData() : null;
        Object meta = apiResponse != null ? apiResponse.getMeta() : null;
        return ResponseEntity.ok(MobileResponse.success(data, meta));
    }

    @PostMapping("/nearest")
    public ResponseEntity<MobileResponse<Object>> findNearestHospitals(@RequestBody NearestReq request) {
        log.info("POST /api/v1/mobile/locations/nearest - Finding nearest hospitals via integration service");
        ApiResponse<Object> apiResponse = integrationServiceClient.findNearestHospitals(request);
        return ResponseEntity.ok(MobileResponse.success(apiResponse != null ? apiResponse.getData() : null));
    }

    @PostMapping("/search")
    public ResponseEntity<MobileResponse<Object>> searchLocations(@RequestBody SearchReq request) {
        log.info("POST /api/v1/mobile/locations/search - Search locations via integration service");
        ApiResponse<Object> apiResponse = integrationServiceClient.searchLocations(request);
        return ResponseEntity.ok(MobileResponse.success(apiResponse != null ? apiResponse.getData() : null));
    }

    @PostMapping("/detail")
    public ResponseEntity<MobileResponse<Object>> getLocationById(@RequestBody DetailReq request) {
        log.info("POST /api/v1/mobile/locations/detail - Fetching location details via integration service for ID: {}", request.getId());
        ApiResponse<Object> apiResponse = integrationServiceClient.getLocationById(request);
        return ResponseEntity.ok(MobileResponse.success(apiResponse != null ? apiResponse.getData() : null));
    }
}