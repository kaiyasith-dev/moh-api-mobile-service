package apb.co.la.moh.api.mobile.service.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.UUID;

@Component
public class TraceIdFilter implements Filter {

    public static final String TRACE_ID = "traceId";
    public static final String TRACE_HEADER = "X-TRACE-ID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 1. Try to get the trace ID from the incoming header
        String traceId = httpRequest.getHeader(TRACE_HEADER);

        // 2. Fallback to generating a new one
        if (!StringUtils.hasText(traceId)) {
            traceId = UUID.randomUUID().toString();
        }

        MDC.put(TRACE_ID, traceId);
        request.setAttribute(TRACE_ID, traceId);

        if (response instanceof HttpServletResponse httpServletResponse) {
            httpServletResponse.setHeader(TRACE_HEADER, traceId);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
