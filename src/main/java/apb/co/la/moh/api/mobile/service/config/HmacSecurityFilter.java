package apb.co.la.moh.api.mobile.service.config;

import apb.co.la.moh.api.mobile.service.dto.MobileResponse;
import apb.co.la.moh.api.mobile.service.enums.ResultCode;
import apb.co.la.moh.api.mobile.service.util.HmacUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class HmacSecurityFilter extends OncePerRequestFilter {

    @Value("${app.security.hmac.enabled:false}")
    private boolean isHmacEnabled;

    @Value("${app.security.hmac.secret-key:}")
    private String secretKey;

    @Value("${app.security.hmac.timeout-seconds:300}")
    private long timeoutSeconds;

    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        return !isHmacEnabled ||
                path.contains("/swagger-ui") ||
                path.contains("/v3/api-docs") ||
                path.contains("/swagger-resources") ||
                path.contains("/webjars/") ||
                path.contains("/actuator");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        CachedServletRequest cachedRequest = new CachedServletRequest(request);

        String clientSignature = request.getHeader("X-Signature");
        String timestampStr = request.getHeader("X-Timestamp");

        if (clientSignature == null || timestampStr == null) {
            sendErrorResponse(response, "Missing X-Signature or X-Timestamp headers");
            return;
        }

        try {
            long requestTime = Long.parseLong(timestampStr);
            long currentTime = Instant.now().getEpochSecond();

            // Prevent replay attacks by checking timestamp staleness
            if (Math.abs(currentTime - requestTime) > timeoutSeconds) {
                sendErrorResponse(response, "Request expired or invalid timestamp");
                return;
            }

            // Construct payload to sign: URI + QueryParams + Timestamp + Body
            String uri = request.getRequestURI();
            String queryString = request.getQueryString() != null ? "?" + request.getQueryString() : "";
            String body = cachedRequest.getBody();

            String payloadToSign = uri + queryString + timestampStr + (body != null ? body : "");
            String calculatedHmac = HmacUtil.calculate(payloadToSign, secretKey);

            if (!calculatedHmac.equalsIgnoreCase(clientSignature)) {
                log.warn("HMAC verification failed for URI: {}", uri);
                sendErrorResponse(response, "Invalid HMAC signature");
                return;
            }

        } catch (NumberFormatException e) {
            sendErrorResponse(response, "Invalid timestamp format");
            return;
        }

        // Proceed with the cached request wrapper
        filterChain.doFilter(cachedRequest, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        MobileResponse<Object> errorResponse = MobileResponse.error(ResultCode.UNAUTHORIZED, message);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}