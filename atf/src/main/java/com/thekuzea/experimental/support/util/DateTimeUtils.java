package com.thekuzea.experimental.support.util;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeUtils {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static String convertOffsetDateTimeToString(final OffsetDateTime offsetDateTime) {
        return offsetDateTime.truncatedTo(ChronoUnit.SECONDS).format(FORMATTER);
    }
}
