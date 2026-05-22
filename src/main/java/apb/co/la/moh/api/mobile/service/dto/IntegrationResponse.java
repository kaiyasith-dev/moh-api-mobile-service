package apb.co.la.moh.api.mobile.service.dto;

import lombok.Data;

@Data
public class IntegrationResponse<T> {
    private String timestamp;
    private boolean success;
    private String code;
    private String message;
    private T data;
    private Object meta;
}
