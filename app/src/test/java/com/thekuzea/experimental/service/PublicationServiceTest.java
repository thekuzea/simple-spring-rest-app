package com.thekuzea.experimental.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import com.thekuzea.experimental.api.dto.PublicationDto;
import com.thekuzea.experimental.domain.model.Publication;
import com.thekuzea.experimental.domain.model.User;
import com.thekuzea.experimental.infrastructure.persistence.PublicationRepository;
import com.thekuzea.experimental.infrastructure.persistence.UserRepository;
import com.thekuzea.experimental.support.constant.messages.entity.PublicationMessages;
import com.thekuzea.experimental.support.util.AuthenticationUtils;
import com.thekuzea.experimental.test.util.model.UserModelTestDataGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static com.thekuzea.experimental.test.util.AuthenticationTestDataGenerator.createUserDetailsForSimpleUser;
import static com.thekuzea.experimental.test.util.dto.PublicationDtoTestDataGenerator.createPublicationDto;
import static com.thekuzea.experimental.test.util.model.PublicationModelTestDataGenerator.createPublication;
import static com.thekuzea.experimental.test.util.model.PublicationModelTestDataGenerator.createPublicationList;

@ExtendWith(MockitoExtension.class)
class PublicationServiceTest {

    private PublicationService publicationService;

    @Mock
    private PublicationRepository publicationRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        publicationService = new PublicationServiceImpl(publicationRepository, userRepository);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(publicationRepository, userRepository);
    }

    @Test
    void shouldGetAllPublications() {
        final List<Publication> publicationList = createPublicationList();
        when(publicationRepository.findAll()).thenReturn(publicationList);

        final List<PublicationDto> actualDtoList = publicationService.getAllPublications();

        verify(publicationRepository).findAll();
        assertThat(actualDtoList).hasSize(3);
    }

    @Test
    void shouldNotGetAllPublications() {
        final List<Publication> publicationList = Collections.emptyList();
        when(publicationRepository.findAll()).thenReturn(publicationList);

        final List<PublicationDto> actualDtoList = publicationService.getAllPublications();

        verify(publicationRepository).findAll();
        assertThat(actualDtoList).isEmpty();
    }

    @Test
    void shouldGetAllPublicationsForCurrentUser() {
        final User user = UserModelTestDataGenerator.createUser();
        final UserDetails userDetails = createUserDetailsForSimpleUser();
        final List<Publication> publicationList = createPublicationList();
        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(Optional.of(user));
        when(publicationRepository.findAllByPublishedBy(any(User.class))).thenReturn(publicationList);

        try (final MockedStatic<AuthenticationUtils> mocked = mockStatic(AuthenticationUtils.class)) {
            mocked.when(AuthenticationUtils::getCurrentLoggedInUser).thenReturn(userDetails);

            final List<PublicationDto> actualPublicationDtos = publicationService.getAllPublicationsForCurrentUser();

            mocked.verify(AuthenticationUtils::getCurrentLoggedInUser);
            assertThat(actualPublicationDtos).hasSize(3);
        }

        verify(userRepository).findByUsername(userDetails.getUsername());
        verify(publicationRepository).findAllByPublishedBy(any(User.class));
    }

    @Test
    void shouldNotGetAllPublicationsForCurrentUser() {
        final User user = UserModelTestDataGenerator.createUser();
        final UserDetails userDetails = createUserDetailsForSimpleUser();
        final List<Publication> publicationList = Collections.emptyList();
        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(Optional.of(user));
        when(publicationRepository.findAllByPublishedBy(any(User.class))).thenReturn(publicationList);

        try (final MockedStatic<AuthenticationUtils> mocked = mockStatic(AuthenticationUtils.class)) {
            mocked.when(AuthenticationUtils::getCurrentLoggedInUser).thenReturn(userDetails);

            final List<PublicationDto> actualPublicationDtos = publicationService.getAllPublicationsForCurrentUser();

            mocked.verify(AuthenticationUtils::getCurrentLoggedInUser);
            assertThat(actualPublicationDtos).isEmpty();
        }

        verify(userRepository).findByUsername(userDetails.getUsername());
        verify(publicationRepository).findAllByPublishedBy(any(User.class));
    }

    @Test
    void shouldAddNewPublication() {
        final PublicationDto expectedPublicationDto = createPublicationDto();
        final Publication publication = createPublication();
        final User publishedBy = publication.getPublishedBy();
        when(publicationRepository.existsByTopic(expectedPublicationDto.getTopic())).thenReturn(false);
        when(userRepository.findByUsername(expectedPublicationDto.getPublishedBy())).thenReturn(Optional.of(publishedBy));

        try (final MockedStatic<AuthenticationUtils> mocked = mockStatic(AuthenticationUtils.class)) {
            mocked.when(AuthenticationUtils::getCurrentLoggedInUser)
                    .thenReturn(createUserDetailsForSimpleUser());

            final PublicationDto actualPublicationDto = publicationService.addNewPublication(expectedPublicationDto);

            mocked.verify(AuthenticationUtils::getCurrentLoggedInUser);
            assertThat(actualPublicationDto).usingRecursiveComparison().isEqualTo(expectedPublicationDto);
        }

        verify(publicationRepository).existsByTopic(expectedPublicationDto.getTopic());
        verify(userRepository).findByUsername(expectedPublicationDto.getPublishedBy());
        verify(publicationRepository).save(any(Publication.class));
    }

    @Test
    void shouldNotAddNewPublicationTopicAlreadyExists() {
        final PublicationDto publicationDto = createPublicationDto();
        when(publicationRepository.existsByTopic(publicationDto.getTopic())).thenReturn(true);

        assertThatThrownBy(() -> publicationService.addNewPublication(publicationDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PublicationMessages.PUBLICATION_ALREADY_EXISTS);

        verify(publicationRepository).existsByTopic(publicationDto.getTopic());
    }

    @Test
    void shouldNotAddNewPublicationWrongPublisher() {
        final PublicationDto publicationDto = createPublicationDto();
        when(publicationRepository.existsByTopic(publicationDto.getTopic())).thenReturn(false);
        when(userRepository.findByUsername(publicationDto.getPublishedBy())).thenReturn(Optional.empty());

        try (final MockedStatic<AuthenticationUtils> mocked = mockStatic(AuthenticationUtils.class)) {
            mocked.when(AuthenticationUtils::getCurrentLoggedInUser)
                    .thenReturn(createUserDetailsForSimpleUser());

            assertThatThrownBy(() -> publicationService.addNewPublication(publicationDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(PublicationMessages.WRONG_PUBLISHER_PROVIDED);

            mocked.verify(AuthenticationUtils::getCurrentLoggedInUser);
        }

        verify(publicationRepository).existsByTopic(publicationDto.getTopic());
        verify(userRepository).findByUsername(publicationDto.getPublishedBy());
    }

    @Test
    void shouldDeletePublicationByTopic() {
        final Publication publication = createPublication();
        final String topic = publication.getTopic();
        when(publicationRepository.findByTopic(topic)).thenReturn(Optional.of(publication));

        publicationService.deleteByTopic(topic);

        verify(publicationRepository).findByTopic(topic);
        verify(publicationRepository).deleteByTopic(topic);
    }

    @Test
    void shouldNotDeletePublicationByTopic() {
        final String topic = "unknown";
        when(publicationRepository.findByTopic(topic)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> publicationService.deleteByTopic(topic))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PublicationMessages.PUBLICATION_NOT_FOUND);

        verify(publicationRepository).findByTopic(topic);
    }
}
