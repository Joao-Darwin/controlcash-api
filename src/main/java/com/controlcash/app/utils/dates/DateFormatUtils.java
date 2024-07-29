package com.controlcash.app.utils.dates;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtils {

    private static DateFormatUtils dateFormatUtils;

    private final SimpleDateFormat simpleDateFormat;

    private DateFormatUtils () {
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    public static DateFormatUtils getInstance() {
        if (dateFormatUtils == null) dateFormatUtils = new DateFormatUtils();

        return dateFormatUtils;
    }

    public SimpleDateFormat getSimpleDateFormat() {
        return this.simpleDateFormat;
    }

    public Date convertStringToDate(String stringDate) throws ParseException {
        return simpleDateFormat.parse(stringDate);
    }
}
