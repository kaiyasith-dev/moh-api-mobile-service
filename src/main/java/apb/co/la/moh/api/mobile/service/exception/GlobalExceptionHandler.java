package apb.co.la.moh.api.mobile.service.exception;

import apb.co.la.moh.api.mobile.service.dto.MobileResponse;
import apb.co.la.moh.api.mobile.service.enums.MobileResultCode;
import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ResponseEntity<MobileResponse<Object>> handleFeignException(FeignException ex) {
        log.error("Feign client error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(MobileResponse.error(MobileResultCode.EXTERNAL_API_ERROR, "Integration Service Error: " + ex.status()), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MobileResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(MobileResponse.error(MobileResultCode.VALIDATION_ERROR, "Invalid request parameters"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MobileResponse<Object>> handleBindException(BindException ex) {
        return new ResponseEntity<>(MobileResponse.error(MobileResultCode.VALIDATION_ERROR, "Invalid request parameters"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MobileResponse<Object>> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(MobileResponse.error(MobileResultCode.VALIDATION_ERROR, "Malformed JSON body"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MobileResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>(MobileResponse.error(MobileResultCode.VALIDATION_ERROR, "Parameter type mismatch: " + ex.getName()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MobileResponse<Object>> handleMissingParam(MissingServletRequestParameterException ex) {
        return new ResponseEntity<>(MobileResponse.error(MobileResultCode.VALIDATION_ERROR, "Missing parameter: " + ex.getParameterName()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MobileResponse<Object>> handleMissingHeader(MissingRequestHeaderException ex) {
        return new ResponseEntity<>(MobileResponse.error(MobileResultCode.VALIDATION_ERROR, "Missing header: " + ex.getHeaderName()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MobileResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        return new ResponseEntity<>(MobileResponse.error(MobileResultCode.VALIDATION_ERROR, "Constraint violation"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<MobileResponse<Object>> handleNoResourceFound(NoResourceFoundException ex) {
        return new ResponseEntity<>(MobileResponse.error(MobileResultCode.DATA_NOT_FOUND, "Resource not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<MobileResponse<Object>> handleNoHandler(NoHandlerFoundException ex) {
        return new ResponseEntity<>(MobileResponse.error(MobileResultCode.DATA_NOT_FOUND, "API endpoint not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<MobileResponse<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return new ResponseEntity<>(MobileResponse.error(MobileResultCode.METHOD_NOT_ALLOWED, "Method not allowed: " + ex.getMethod()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<MobileResponse<Object>> handleGenericException(Exception ex) {
        log.error("Internal server error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(MobileResponse.error(MobileResultCode.ERROR, "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
