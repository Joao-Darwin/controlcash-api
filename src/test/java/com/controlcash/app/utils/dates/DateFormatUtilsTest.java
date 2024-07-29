package com.controlcash.app.utils.dates;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtilsTest {

    private DateFormatUtils dateFormatUtils;

    @BeforeEach
    void setUp() {
        this.dateFormatUtils = DateFormatUtils.getInstance();
    }

    @Test
    void testGetSimpleDateFormat_ShouldReturnDefaultSimpleDateFormat() {
        SimpleDateFormat simpleDateFormat = dateFormatUtils.getSimpleDateFormat();

        Assertions.assertNotNull(simpleDateFormat);
        Assertions.assertEquals("dd/MM/yyyy", simpleDateFormat.toPattern());
    }

    @ParameterizedTest
    @CsvSource({
            "19/07/2024",
            "19/07/24",
            "19/07/2024 00:00:00:0"
    })
    void testConvertStringToDate_GivenAValidString_ShouldReturnDateBasedOnString(String stringDate) {
        Date actualDate = Assertions.assertDoesNotThrow(() -> dateFormatUtils.convertStringToDate(stringDate));

        Assertions.assertNotNull(actualDate);
    }

    @ParameterizedTest
    @CsvSource({
            "19/07",
            "07/2024",
    })
    void testConvertStringToDate_GivenANotValidString_ShouldThrowsAParseException(String stringDate) {
        Assertions.assertThrows(ParseException.class, () -> dateFormatUtils.convertStringToDate(stringDate));
    }
}
