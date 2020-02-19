package com.thekuzea.experimental.api.controller;

import java.util.List;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.thekuzea.experimental.api.dto.PublicationDto;

@RequestMapping("/publication")
public interface PublicationController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<PublicationDto> getAllPublications();

    @GetMapping(value = "/current-user-publications", produces = MediaType.APPLICATION_JSON_VALUE)
    List<PublicationDto> getAllPublicationsForCurrentUser();

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    PublicationDto publish(@Valid @RequestBody PublicationDto dto);

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{topic}")
    void deleteByTopic(@PathVariable("topic") String topic);
}
