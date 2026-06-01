package apb.co.la.moh.api.mobile.service.dto;

import apb.co.la.moh.api.mobile.service.enums.ResultCode;
import apb.co.la.moh.api.mobile.service.util.RequestUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    /**
     * Standard API header
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MobileHeader {

        private String timestamp;
        private String code;
        private String message;
        private String status;
        private String traceId;
    }

    /**
     * Wrapper for responses that include pagination or extra metadata
     * meta will be excluded automatically if null
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BodyWithMeta<D, M> {
        private D data;
        private M meta;
    }

    // =========================
    // SUCCESS (simple body)
    // =========================
    public static <T> MobileResponse<T> success(T body) {
        return buildResponse(
                ResultCode.SUCCESS,
                ResultCode.SUCCESS.getMessage(),
                body
        );
    }

    // =========================
    // SUCCESS (data + meta)
    // =========================
    public static <D, M> MobileResponse<BodyWithMeta<D, M>> success(D data, M meta) {

        BodyWithMeta<D, M> body = BodyWithMeta.<D, M>builder()
                .data(data)
                .meta(meta)
                .build();

        return buildResponse(
                ResultCode.SUCCESS,
                ResultCode.SUCCESS.getMessage(),
                body
        );
    }

    // =========================
    // ERROR RESPONSE
    // =========================
    public static <T> MobileResponse<T> error(
            ResultCode code,
            String message
    ) {
        return buildResponse(
                code,
                message != null ? message : code.getMessage(),
                null
        );
    }

    // =========================
    // CORE BUILDER
    // =========================
    private static <T> MobileResponse<T> buildResponse(
            ResultCode resultCode,
            String message,
            T body
    ) {

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