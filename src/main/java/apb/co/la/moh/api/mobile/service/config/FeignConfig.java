package apb.co.la.moh.api.mobile.service.config;

import apb.co.la.moh.api.mobile.service.util.RequestUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                String traceId = RequestUtil.getCurrentTraceId();
                if (StringUtils.hasText(traceId)) {
                    template.header(TraceIdFilter.TRACE_HEADER, traceId);
                }
            }
        };
    }
}
