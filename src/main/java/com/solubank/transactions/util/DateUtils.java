package com.solubank.transactions.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class DateUtils {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private DateUtils() {
    }

    public static String formaterDate(LocalDateTime date) {
        return date.format(DATETIME_FORMAT);
    }

    public static Optional<LocalDate> parserDate(String texte) {
        try {
            return Optional.of(LocalDate.parse(texte, DATE_FORMAT));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
}