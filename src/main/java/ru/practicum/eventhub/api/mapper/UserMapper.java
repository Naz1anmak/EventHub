package ru.practicum.eventhub.api.mapper;

import org.mapstruct.*;
import ru.practicum.eventhub.api.dto.request.UserCreateDto;
import ru.practicum.eventhub.api.dto.request.UserMetadataCreateDto;
import ru.practicum.eventhub.api.dto.request.UserMetadataUpdateDto;
import ru.practicum.eventhub.api.dto.request.UserUpdateDto;
import ru.practicum.eventhub.api.dto.response.UserDto;
import ru.practicum.eventhub.domain.model.User;
import ru.practicum.eventhub.domain.model.UserMetadata;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = SPRING)
public interface UserMapper {
    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "metadata", ignore = true)
    User fromCreateDto(UserCreateDto dto);

    @AfterMapping
    default void afterCreate(UserCreateDto dto, @MappingTarget User user) {
        UserMetadata metadata = metadataFromNestedDto(dto.metadata());
        user.setMetadata(metadata);
    }

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "metadata", ignore = true)
    User updateUserFromDto(UserUpdateDto dto, @MappingTarget User user);

    @AfterMapping
    default void afterUpdate(UserUpdateDto dto, @MappingTarget User user) {
        if (dto.metadata() == null) {
            return;
        }
        updateMetadataFromDto(dto.metadata(), user.getMetadata());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserMetadata metadataFromNestedDto(UserMetadataCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateMetadataFromDto(UserMetadataUpdateDto dto, @MappingTarget UserMetadata metadata);
}
