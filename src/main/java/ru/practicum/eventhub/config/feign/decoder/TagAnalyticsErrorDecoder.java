package ru.practicum.eventhub.config.feign.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.practicum.eventhub.config.feign.exception.TagAnalyticsClientException;
import ru.practicum.eventhub.config.feign.exception.TagNotFoundException;

public class TagAnalyticsErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() >= 400 && response.status() < 500) {
            if (response.status() == 404) {
                return new TagNotFoundException("Ресурс не найден для метода: " + methodKey);
            }
            return new TagAnalyticsClientException("Ошибка клиента при вызове метода: " + methodKey + " с кодом статуса: " + response.status());
        }

        return defaultDecoder.decode(methodKey, response);
    }
}
