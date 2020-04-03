package com.thekuzea.experimental.support.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import com.thekuzea.experimental.support.constant.RestConstants;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RestUtils {

    public static String formUri(final String... pathParameters) {
        return String.join(RestConstants.URI_SEPARATOR, pathParameters);
    }

    public static String formToken(final String... tokenParameters) {
        return String.join(StringUtils.SPACE, tokenParameters);
    }
}
