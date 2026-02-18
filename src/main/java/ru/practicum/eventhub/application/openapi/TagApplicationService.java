package ru.practicum.eventhub.application.openapi;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.eventhub.api.dto.response.TagDto;
import ru.practicum.eventhub.api.model.PageOfTags;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.service.TagService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TagApplicationService {
    private final TagService tagService;

    public PageOfTags getTags(Pageable pageable) {
        PagedResponse<TagDto> page = tagService.getTags(pageable);
        return getPageOfTags(page);
    }

    public PageOfTags getTagsByEvent(UUID eventId, Pageable pageable) {
        PagedResponse<TagDto> page = tagService.getTagsByEvent(eventId, pageable);
        return getPageOfTags(page);
    }

    private static @NotNull PageOfTags getPageOfTags(PagedResponse<TagDto> page) {
        PageOfTags apiPage = new PageOfTags();
        apiPage.setPage(page.page());
        apiPage.setSize(page.size());
        apiPage.setTotalElements(page.totalElements());
        apiPage.setTotalPages(page.totalPages());
        apiPage.setContent(page.content());
        return apiPage;
    }
}
