package ru.practicum.eventhub.application.openapi;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.eventhub.api.dto.response.CategoryDto;
import ru.practicum.eventhub.api.model.PageOfCategories;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.service.CategoryService;

@Service
@RequiredArgsConstructor
public class CategoryApplicationService {
    private final CategoryService categoryService;

    public PageOfCategories getCategories(Pageable pageable) {
        PagedResponse<CategoryDto> page = categoryService.getCategories(pageable);
        return getPageOfCategories(page);
    }

    private static @NotNull PageOfCategories getPageOfCategories(PagedResponse<CategoryDto> page) {
        PageOfCategories apiPage = new PageOfCategories();
        apiPage.setPage(page.page());
        apiPage.setSize(page.size());
        apiPage.setTotalElements(page.totalElements());
        apiPage.setTotalPages(page.totalPages());
        apiPage.setContent(page.content());
        return apiPage;
    }
}
