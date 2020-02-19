package com.thekuzea.experimental.domain.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.thekuzea.experimental.api.dto.PublicationDto;
import com.thekuzea.experimental.domain.model.Publication;
import com.thekuzea.experimental.support.util.DateTimeUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PublicationMapper {

    public static Publication mapToModel(final PublicationDto dto) {
        return Publication.builder()
                .publicationTime(DateTimeUtils.convertStringToOffsetDateTime(dto.getPublicationTime()))
                .topic(dto.getTopic())
                .body(dto.getBody())
                .build();
    }

    public static PublicationDto mapToDto(final Publication entity) {
        return PublicationDto.builder()
                .id(entity.getId().toString())
                .publishedBy(entity.getPublishedBy().getUsername())
                .publicationTime(DateTimeUtils.convertOffsetDateTimeToString(entity.getPublicationTime()))
                .topic(entity.getTopic())
                .body(entity.getBody())
                .build();
    }
}
