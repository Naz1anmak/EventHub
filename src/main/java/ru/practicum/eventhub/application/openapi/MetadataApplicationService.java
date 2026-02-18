package ru.practicum.eventhub.application.openapi;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.eventhub.api.dto.response.UserMetadataDto;
import ru.practicum.eventhub.api.model.PageOfMetadata;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.service.UserMetadataService;

@Service
@RequiredArgsConstructor
public class MetadataApplicationService {
    private final UserMetadataService userMetadataService;

    public PageOfMetadata getUserMetadata(Pageable pageable) {
        PagedResponse<UserMetadataDto> page = userMetadataService.getUserMetadata(pageable);
        return getUserMetadata(page);
    }

    private static @NotNull PageOfMetadata getUserMetadata(PagedResponse<UserMetadataDto> page) {
        PageOfMetadata pageOfMetadata = new PageOfMetadata();
        pageOfMetadata.setPage(page.page());
        pageOfMetadata.setSize(page.size());
        pageOfMetadata.setTotalElements(page.totalElements());
        pageOfMetadata.setTotalPages(page.totalPages());
        pageOfMetadata.setContent(page.content());
        return pageOfMetadata;
    }
}
