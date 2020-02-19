package com.thekuzea.experimental.api.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import com.thekuzea.experimental.api.dto.PublicationDto;
import com.thekuzea.experimental.service.PublicationService;

@RestController
@RequiredArgsConstructor
public class PublicationControllerImpl implements PublicationController {

    private final PublicationService publicationService;

    @Override
    public List<PublicationDto> getAllPublications() {
        return publicationService.getAllPublications();
    }

    @Override
    public List<PublicationDto> getAllPublicationsForCurrentUser() {
        return publicationService.getAllPublicationsForCurrentUser();
    }

    @Override
    public PublicationDto publish(final PublicationDto dto) {
        return publicationService.addNewPublication(dto);
    }

    @Override
    public void deleteByTopic(final String topic) {
        publicationService.deleteByTopic(topic);
    }
}
