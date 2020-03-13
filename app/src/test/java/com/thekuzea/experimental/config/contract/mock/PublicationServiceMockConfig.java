package com.thekuzea.experimental.config.contract.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.thekuzea.experimental.service.PublicationService;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static com.thekuzea.experimental.support.constant.messages.entity.PublicationMessages.PUBLICATION_ALREADY_EXISTS;
import static com.thekuzea.experimental.support.constant.messages.entity.PublicationMessages.PUBLICATION_NOT_FOUND;
import static com.thekuzea.experimental.support.constant.messages.entity.PublicationMessages.WRONG_PUBLISHER_PROVIDED;
import static com.thekuzea.experimental.test.util.dto.PublicationDtoTestDataGenerator.createPublicationDtoAsNewPublication;
import static com.thekuzea.experimental.test.util.dto.PublicationDtoTestDataGenerator.createPublicationDtoAsNewPublicationWhereItAlreadyExists;
import static com.thekuzea.experimental.test.util.dto.PublicationDtoTestDataGenerator.createPublicationDtoAsNewPublicationWrongPublisher;
import static com.thekuzea.experimental.test.util.dto.PublicationDtoTestDataGenerator.createPublicationDtoList;
import static com.thekuzea.experimental.test.util.dto.PublicationDtoTestDataGenerator.createPublicationDtoListForCurrentUser;

@TestConfiguration
@Profile("contract")
public class PublicationServiceMockConfig {

    @Bean
    @Primary
    public PublicationService publicationService() {
        final PublicationService publicationService = mock(PublicationService.class);

        when(publicationService.getAllPublications()).thenReturn(createPublicationDtoList());
        when(publicationService.getAllPublicationsForCurrentUser()).thenReturn(createPublicationDtoListForCurrentUser());
        doThrow(new IllegalArgumentException(PUBLICATION_NOT_FOUND)).when(publicationService).deleteByTopic("Collinsia heterophylla");

        when(publicationService.addNewPublication(refEq(createPublicationDtoAsNewPublication())))
                .thenReturn(createPublicationDtoAsNewPublication());
        when(publicationService.addNewPublication(refEq(createPublicationDtoAsNewPublicationWhereItAlreadyExists())))
                .thenThrow(new IllegalArgumentException(PUBLICATION_ALREADY_EXISTS));
        when(publicationService.addNewPublication(refEq(createPublicationDtoAsNewPublicationWrongPublisher())))
                .thenThrow(new IllegalArgumentException(WRONG_PUBLISHER_PROVIDED));

        return publicationService;
    }
}
