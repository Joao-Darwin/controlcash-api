package com.controlcash.app.utils.dates;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DateFormatUtils {

    private static DateFormatUtils dateFormatUtils;

    private final List<DateTimeFormatter> formatters;

    private DateFormatUtils() {
        formatters = new ArrayList<>();
        formatters.add(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        formatters.add(DateTimeFormatter.ofPattern("dd/MM/yy"));
        formatters.add(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss:S"));
    }

    public static DateFormatUtils getInstance() {
        if (dateFormatUtils == null) dateFormatUtils = new DateFormatUtils();

        return dateFormatUtils;
    }

    public LocalDate convertStringToDate(String stringDate) {
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(stringDate, formatter);
            } catch (DateTimeParseException ignored) {}
        }

        throw new DateTimeParseException("Invalid date format: " + stringDate, stringDate, 0);
    }
}
