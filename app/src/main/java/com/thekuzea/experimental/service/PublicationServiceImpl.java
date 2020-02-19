package com.thekuzea.experimental.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thekuzea.experimental.support.constant.messages.logging.LoggingMessages;
import com.thekuzea.experimental.infrastructure.persistence.PublicationRepository;
import com.thekuzea.experimental.infrastructure.persistence.UserRepository;
import com.thekuzea.experimental.api.dto.PublicationDto;
import com.thekuzea.experimental.domain.mapper.PublicationMapper;
import com.thekuzea.experimental.domain.model.Publication;
import com.thekuzea.experimental.domain.model.User;
import com.thekuzea.experimental.support.util.AuthenticationUtils;

import static com.thekuzea.experimental.support.constant.messages.entity.PublicationMessages.PUBLICATION_ALREADY_EXISTS;
import static com.thekuzea.experimental.support.constant.messages.entity.PublicationMessages.PUBLICATION_NOT_FOUND;
import static com.thekuzea.experimental.support.constant.messages.entity.PublicationMessages.WRONG_PUBLISHER_PROVIDED;
import static com.thekuzea.experimental.support.constant.messages.logging.LoggingMessages.USER_NOT_FOUND_BY_USERNAME;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicationServiceImpl implements PublicationService {

    private final PublicationRepository publicationRepository;

    private final UserRepository userRepository;

    @Override
    public List<PublicationDto> getAllPublications() {
        return publicationRepository.findAll()
                .stream()
                .map(PublicationMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicationDto> getAllPublicationsForCurrentUser() {
        final UserDetails currentLoggedInUser = AuthenticationUtils.getCurrentLoggedInUser();
        final Optional<User> foundUser = userRepository.findByUsername(currentLoggedInUser.getUsername());
        List<PublicationDto> foundPublications = Collections.emptyList();

        if (foundUser.isPresent()) {
            foundPublications = publicationRepository.findAllByPublishedBy(foundUser.get())
                    .stream()
                    .map(PublicationMapper::mapToDto)
                    .collect(Collectors.toList());
        }

        return foundPublications;
    }

    @Override
    @Transactional
    public PublicationDto addNewPublication(final PublicationDto dto) {
        final boolean publicationExists = publicationRepository.existsByTopic(dto.getTopic());

        if (!publicationExists) {
            final String currentLoggedInUser = AuthenticationUtils.getCurrentLoggedInUser().getUsername();
            final Optional<User> foundUser = userRepository.findByUsername(currentLoggedInUser);

            if (foundUser.isPresent()) {
                final Publication mappedEntity = PublicationMapper.mapToModel(dto);
                mappedEntity.setPublishedBy(foundUser.get());
                publicationRepository.save(mappedEntity);
            } else {
                log.error(USER_NOT_FOUND_BY_USERNAME, dto.getPublishedBy());
                throw new IllegalArgumentException(WRONG_PUBLISHER_PROVIDED);
            }
        } else {
            log.debug(LoggingMessages.PUBLICATION_ALREADY_EXISTS_LOG, dto.getTopic());
            throw new IllegalArgumentException(PUBLICATION_ALREADY_EXISTS);
        }

        return dto;
    }

    @Override
    @Transactional
    public void deleteByTopic(final String topic) {
        final Optional<Publication> possiblePublication = publicationRepository.findByTopic(topic);

        if (possiblePublication.isPresent()) {
            publicationRepository.deleteByTopic(topic);
            log.debug(LoggingMessages.DELETED_PUBLICATION, possiblePublication.get().getTopic());
        } else {
            log.error(LoggingMessages.PUBLICATION_NOT_FOUND_BY_TOPIC, topic);
            throw new IllegalArgumentException(PUBLICATION_NOT_FOUND);
        }
    }
}
