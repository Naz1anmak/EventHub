package ru.practicum.eventhub.api.mapper;

import org.mapstruct.*;
import ru.practicum.eventhub.api.dto.request.EventCreateDto;
import ru.practicum.eventhub.api.dto.request.EventUpdateDto;
import ru.practicum.eventhub.api.dto.response.EventDto;
import ru.practicum.eventhub.domain.model.Category;
import ru.practicum.eventhub.domain.model.Event;
import ru.practicum.eventhub.domain.model.Tag;
import ru.practicum.eventhub.domain.model.User;

import java.util.Set;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface EventMapper {
    EventDto toDto(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "description", source = "createDto.description")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "tags", source = "tags")
    Event fromCreateDto(EventCreateDto createDto, Category category, User createdBy, Set<Tag> tags);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "description", source = "updateDto.description")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "createdBy", source = "createdBy")
    void updateEventFromDto(EventUpdateDto updateDto, @MappingTarget Event event, Category category, User createdBy);
}
