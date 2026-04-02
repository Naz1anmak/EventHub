package ru.practicum.eventhub.config.feign.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.eventhub.config.feign.decoder.TagAnalyticsErrorDecoder;

@Configuration
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new TagAnalyticsErrorDecoder();
    }
}
