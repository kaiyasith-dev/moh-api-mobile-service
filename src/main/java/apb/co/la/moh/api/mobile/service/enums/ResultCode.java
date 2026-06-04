package apb.co.la.moh.api.mobile.service.enums;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS("0000", "01", "Success"),
    HOLD("0001", "02", "Transaction on hold"),
    REVERT("0002", "03", "Transaction should revert"),
    ERROR("5000", "04", "Internal server error"),
    EXTERNAL_API_ERROR("5002", "04", "External API error"),
    VALIDATION_ERROR("4000", "04", "Invalid request parameters"),
    UNAUTHORIZED("4001", "04", "Unauthorized request"),
    DATA_NOT_FOUND("4004", "04", "Resource not found"),
    METHOD_NOT_ALLOWED("4005", "04", "Method not allowed");

    private final String code;
    private final String status;
    private final String message;

    ResultCode(String code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
