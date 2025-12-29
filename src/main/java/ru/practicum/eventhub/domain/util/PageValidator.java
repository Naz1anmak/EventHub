package ru.practicum.eventhub.domain.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import ru.practicum.eventhub.api.exception.BadRequestException;

@Slf4j
public final class PageValidator {
    private PageValidator() {
    }

    public static void validatePage(Page<?> page) {
        int requested = page.getNumber();
        int totalPages = page.getTotalPages();

        if (totalPages == 0 && requested > 0
                || totalPages > 0 && requested >= totalPages) {

            log.error("Запрошена страница {}, которой не существует", requested);
            throw new BadRequestException("Запрошена страница " + requested + ", которой не существует");
        }
    }
}
