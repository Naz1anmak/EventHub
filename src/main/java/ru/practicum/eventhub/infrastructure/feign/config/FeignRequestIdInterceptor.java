package ru.practicum.eventhub.infrastructure.feign.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class FeignRequestIdInterceptor implements RequestInterceptor {
    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final String REQUEST_ID_MDC_KEY = "requestId";

    @Override
    public void apply(RequestTemplate template) {
        String requestId = MDC.get(REQUEST_ID_MDC_KEY);
        if (requestId != null) {
            template.header(REQUEST_ID_HEADER, requestId);
        }
    }
}
