package apb.co.la.moh.api.mobile.service.dto;

import apb.co.la.moh.api.mobile.service.enums.MobileResultCode;
import apb.co.la.moh.api.mobile.service.util.RequestUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobileResponse<T> {
    private MobileHeader header;
    private T body;

    public static <T> MobileResponse<T> success(T body) {
        return buildResponse(MobileResultCode.SUCCESS, MobileResultCode.SUCCESS.getMessage(), body);
    }

    public static <T> MobileResponse<T> error(MobileResultCode resultCode, String customMessage) {
        return buildResponse(resultCode, customMessage != null ? customMessage : resultCode.getMessage(), null);
    }

    private static <T> MobileResponse<T> buildResponse(MobileResultCode resultCode, String message, T body) {
        String traceId = RequestUtil.getCurrentTraceId();
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
        }

        return MobileResponse.<T>builder()
                .header(MobileHeader.builder()
                        .timestamp(DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
                        .code(resultCode.getCode())
                        .message(message)
                        .status(resultCode.getStatus())
                        .traceId(traceId)
                        .build())
                .body(body)
                .build();
    }
}
