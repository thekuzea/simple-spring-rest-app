package com.thekuzea.experimental.domain.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PublicationDto {

    String id;

    String publishedBy;

    String publicationTime;

    String topic;

    String body;
}
