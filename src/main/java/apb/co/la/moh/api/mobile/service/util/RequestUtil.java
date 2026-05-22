package apb.co.la.moh.api.mobile.service.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestUtil {
    public static String getCurrentTraceId() {
        HttpServletRequest request = getCurrentRequest();
        if (request != null) {
            Object traceId = request.getAttribute("traceId");
            return traceId != null ? traceId.toString() : null;
        }
        return null;
    }

    private static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
}
