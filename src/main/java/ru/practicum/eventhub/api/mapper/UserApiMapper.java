package ru.practicum.eventhub.api.mapper;

import org.mapstruct.Mapper;
import ru.practicum.eventhub.api.dto.request.UserCreateDto;
import ru.practicum.eventhub.api.dto.request.UserUpdateDto;
import ru.practicum.eventhub.api.dto.response.UserDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserApiMapper {

    UserCreateDto toServiceDto(ru.practicum.eventhub.api.model.UserCreateDto apiDto);

    UserUpdateDto toServiceDto(ru.practicum.eventhub.api.model.UserUpdateDto apiDto);

    ru.practicum.eventhub.api.model.UserDto toApiDto(UserDto serviceDto);

    default ru.practicum.eventhub.api.model.PagedUserDtoResponse toApiPage(PagedResponse<UserDto> page) {
        var apiPage = new ru.practicum.eventhub.api.model.PagedUserDtoResponse();

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
