package apb.co.la.moh.api.mobile.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobileHeader {
    private String timestamp;
    private String code;
    private String message;
    private String status;
    private String traceId;
}
