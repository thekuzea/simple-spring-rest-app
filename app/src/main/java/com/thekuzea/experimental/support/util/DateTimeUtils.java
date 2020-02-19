package com.thekuzea.experimental.support.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeUtils {

    private static final ZoneOffset CURRENT_OFFSET = ZoneOffset.UTC;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static Date convertLocalDateTimeToDate(final LocalDateTime localDateTime) {
        return Date.from(
                localDateTime.atZone(CURRENT_OFFSET).toInstant()
        );
    }

    public static LocalDateTime convertDateToLocalDateTime(final Date date) {
        return date.toInstant()
                .atZone(CURRENT_OFFSET)
                .toLocalDateTime();
    }

    public static String convertOffsetDateTimeToString(final OffsetDateTime offsetDateTime) {
        return FORMATTER.format(offsetDateTime.truncatedTo(ChronoUnit.SECONDS));
    }

    public static OffsetDateTime convertStringToOffsetDateTime(final String dateTime) {
        return OffsetDateTime.parse(dateTime, FORMATTER).truncatedTo(ChronoUnit.SECONDS);
    }
}
