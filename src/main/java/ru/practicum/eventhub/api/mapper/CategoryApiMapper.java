package ru.practicum.eventhub.api.mapper;

import org.mapstruct.Mapper;
import ru.practicum.eventhub.api.dto.request.CategoryCreateDto;
import ru.practicum.eventhub.api.dto.request.CategoryUpdateDto;
import ru.practicum.eventhub.api.dto.response.CategoryDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CategoryApiMapper {

    CategoryCreateDto toServiceDto(ru.practicum.eventhub.api.model.CategoryCreateDto apiDto);

    CategoryUpdateDto toServiceDto(ru.practicum.eventhub.api.model.CategoryUpdateDto apiDto);

    ru.practicum.eventhub.api.model.CategoryDto toApiDto(CategoryDto serviceDto);

    default ru.practicum.eventhub.api.model.PagedCategoryDtoResponse toApiPage(PagedResponse<CategoryDto> page) {
        var apiPage = new ru.practicum.eventhub.api.model.PagedCategoryDtoResponse();

        apiPage.setContent(
                page.content().stream()
                        .map(this::toApiDto)
                        .toList()
        );
        apiPage.setPage(page.page());
        apiPage.setSize(page.size());
        apiPage.setTotalElements(page.totalElements());
        apiPage.setTotalPages(page.totalPages());

        return apiPage;
    }
}
