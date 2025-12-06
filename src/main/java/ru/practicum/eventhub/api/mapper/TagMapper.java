package ru.practicum.eventhub.api.mapper;

import org.mapstruct.*;
import ru.practicum.eventhub.api.dto.request.TagCreateDto;
import ru.practicum.eventhub.api.dto.request.TagUpdateDto;
import ru.practicum.eventhub.api.dto.response.TagDto;
import ru.practicum.eventhub.domain.model.Tag;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDto toDto(Tag tag);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "events", ignore = true)
    Tag fromCreateDto(TagCreateDto createDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "events", ignore = true)
    void updateTagFromDto(TagUpdateDto updateDto, @MappingTarget Tag tag);
}
