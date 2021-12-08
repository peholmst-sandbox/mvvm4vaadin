package net.pkhapps.mvvm4vaadin.demo.ui;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public final class DateFormatters {

    private DateFormatters() {
    }

    public static String formatDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(zonedDateTime);
    }

    public static String formatDateTime(Instant instant) {
        return instant == null ? null : formatDateTime(instant.atZone(ZoneId.systemDefault()));
    }
}
