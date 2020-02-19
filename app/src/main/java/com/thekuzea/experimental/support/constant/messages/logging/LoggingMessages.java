package com.thekuzea.experimental.support.constant.messages.logging;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoggingMessages {

    public static final String USER_NOT_FOUND_BY_USERNAME = "User with username {} not found.";

    public static final String USER_NOT_FOUND_BY_ID = "User with id {} not found.";

    public static final String USER_ALREADY_EXISTS_LOG = "User with username {} already exists.";

    public static final String SAVED_NEW_USER = "User with [{}] username was saved.";

    public static final String UPDATED_USER = "User with [{}] ID was updated.";

    public static final String DELETED_USER = "User with [{}] username was deleted.";

    public static final String ROLE_NOT_FOUND_BY_NAME = "Role with name {} not found.";

    public static final String ROLE_ALREADY_EXISTS_LOG = "Role with name {} already exists.";

    public static final String SAVED_NEW_ROLE = "Role with [{}] name was saved.";

    public static final String DELETED_ROLE = "Role with [{}] name was deleted.";

    public static final String UNABLE_TO_GET_TOKEN = "Unable to get JWT Token.";

    public static final String TOKEN_HAS_EXPIRED = "JWT Token has expired.";

    public static final String TOKEN_FORMAT_WARNING = "JWT Token does not begin with Bearer String.";

    public static final String PUBLICATION_NOT_FOUND_BY_TOPIC = "Publication with topic {} not found.";

    public static final String PUBLICATION_ALREADY_EXISTS_LOG = "Publication with topic {} already exists.";

    public static final String DELETED_PUBLICATION = "Publication with [{}] topic was deleted.";
}
