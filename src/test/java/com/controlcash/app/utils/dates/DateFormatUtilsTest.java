package com.controlcash.app.utils.dates;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateFormatUtilsTest {

    private DateFormatUtils dateFormatUtils;

    @BeforeEach
    void setUp() {
        this.dateFormatUtils = DateFormatUtils.getInstance();
    }

    @ParameterizedTest
    @CsvSource({
            "19/07/2024",
            "19/07/24",
            "19/07/2024 00:00:00:0"
    })
    void testConvertStringToDate_GivenAValidString_ShouldReturnDateBasedOnString(String stringDate) {
        LocalDate actualDate = Assertions.assertDoesNotThrow(() -> dateFormatUtils.convertStringToDate(stringDate));

        Assertions.assertNotNull(actualDate);
    }

    @ParameterizedTest
    @CsvSource({
            "19/07",
            "07/2024",
    })
    void testConvertStringToDate_GivenANotValidString_ShouldThrowsADateTimeParseException(String stringDate) {
        String expectedExceptionMessage = "Invalid date format: " + stringDate;

        DateTimeParseException dateTimeParseException = Assertions.assertThrows(DateTimeParseException.class, () -> dateFormatUtils.convertStringToDate(stringDate));

        Assertions.assertEquals(expectedExceptionMessage, dateTimeParseException.getMessage());
    }

    @Test
    void testGetInstance_ShouldReturnTheSameInstance() {
        DateFormatUtils firstInstance = DateFormatUtils.getInstance();
        DateFormatUtils secondInstance = DateFormatUtils.getInstance();

        Assertions.assertEquals(firstInstance, secondInstance);
    }
}
