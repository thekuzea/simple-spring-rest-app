package com.thekuzea.experimental.support.constant.messages.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PublicationMessages {

    public static final String PUBLICATION_NOT_FOUND = "Publication not found by provided topic.";

    public static final String WRONG_PUBLISHER_PROVIDED = "Wrong publisher provided.";

    public static final String PUBLICATION_ALREADY_EXISTS = "Publication already exists.";
}
