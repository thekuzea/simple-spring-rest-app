package com.thekuzea.experimental.service;

import java.util.List;

import com.thekuzea.experimental.api.dto.PublicationDto;

public interface PublicationService {

    List<PublicationDto> getAllPublications();

    List<PublicationDto> getAllPublicationsForCurrentUser();

    PublicationDto addNewPublication(PublicationDto dto);

    void deleteByTopic(String topic);
}
