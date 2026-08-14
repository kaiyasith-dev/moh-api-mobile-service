package apb.co.la.moh.api.mobile.service.client;

import apb.co.la.moh.api.mobile.service.config.FeignConfig;
import apb.co.la.moh.api.mobile.service.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "integration-service", url = "${app.integration-service.url}", configuration = FeignConfig.class)
public interface ApiServiceClient {

//    @PostMapping("/api/v1/locations")
//    ApiResponse<Object> getAllLocations(@RequestBody Object request);
//
//    @PostMapping("/api/v1/locations/nearest")
//    ApiResponse<Object> findNearestHospitals(@RequestBody Object request);
//
//    @PostMapping("/api/v1/locations/search")
//    ApiResponse<Object> searchLocations(@RequestBody Object request);
//
//    @PostMapping("/api/v1/locations/detail")
//    ApiResponse<Object> getLocationById(@RequestBody Object request);

    @PostMapping("/all")
    ApiResponse<Object> getAllLocations(@RequestBody Object request);

    @PostMapping("/nearest")
    ApiResponse<Object> findNearestHospitals(@RequestBody Object request);

    @PostMapping("/search")
    ApiResponse<Object> searchLocations(@RequestBody Object request);

    @PostMapping("/detail")
    ApiResponse<Object> getLocationById(@RequestBody Object request);
}