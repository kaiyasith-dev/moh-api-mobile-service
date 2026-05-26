package apb.co.la.moh.api.mobile.service.client;

import apb.co.la.moh.api.mobile.service.config.FeignConfig;
import apb.co.la.moh.api.mobile.service.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "integration-service", url = "${app.integration-service.url}", configuration = FeignConfig.class)
public interface ApiServiceClient {

    @GetMapping("/api/v1/locations")
    ApiResponse<Object> getAllLocations(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size);

    @GetMapping("/api/v1/locations/nearest")
    ApiResponse<Object> findNearestHospitals(@RequestParam("latitude") Double latitude,
                                             @RequestParam("longitude") Double longitude,
                                             @RequestParam("radiusKm") Double radiusKm);

    @GetMapping("/api/v1/locations/search")
    ApiResponse<Object> searchLocations(@RequestParam(value = "keyword", required = false) String keyword,
                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size);

    @GetMapping("/api/v1/locations/parent/{parentId}")
    ApiResponse<Object> getLocationsByParentId(String parentId);

    @GetMapping("/api/v1/locations/{id}")
    ApiResponse<Object> getLocationById(String id);
}
