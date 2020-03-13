package com.thekuzea.experimental.domain.mapper;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import com.thekuzea.experimental.api.dto.PublicationDto;
import com.thekuzea.experimental.domain.model.Publication;

import static com.thekuzea.experimental.test.util.dto.PublicationDtoTestDataGenerator.createPublicationDto;
import static com.thekuzea.experimental.test.util.model.PublicationModelTestDataGenerator.createPublication;

class PublicationMapperTest {

    @Test
    void shouldMapDtoToModel() {
        final PublicationDto publicationDto = createPublicationDto();
        final Publication expectedPublicationModel = createPublication();

        final Publication actualPublicationModel = PublicationMapper.mapToModel(publicationDto);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(actualPublicationModel.getPublicationTime()).isEqualTo(expectedPublicationModel.getPublicationTime());
            softly.assertThat(actualPublicationModel.getTopic()).isEqualTo(expectedPublicationModel.getTopic());
            softly.assertThat(actualPublicationModel.getBody()).isEqualTo(expectedPublicationModel.getBody());
        });
    }

    @Test
    void shouldMapModelToDto() {
        final PublicationDto expectedPublicationDto = createPublicationDto();
        final Publication publicationModel = createPublication();

        final PublicationDto actualPublicationDto = PublicationMapper.mapToDto(publicationModel);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(actualPublicationDto.getId()).isEqualTo(expectedPublicationDto.getId());
            softly.assertThat(actualPublicationDto.getPublishedBy()).isEqualTo(expectedPublicationDto.getPublishedBy());
            softly.assertThat(actualPublicationDto.getPublicationTime()).isEqualTo(expectedPublicationDto.getPublicationTime());
            softly.assertThat(actualPublicationDto.getTopic()).isEqualTo(expectedPublicationDto.getTopic());
            softly.assertThat(actualPublicationDto.getBody()).isEqualTo(expectedPublicationDto.getBody());
        });
    }
}
