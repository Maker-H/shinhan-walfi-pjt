package com.shinhan.walfi.util;

import org.springframework.stereotype.Component;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateConversionUtil {

    public Date convertStringToDate(String dateString) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        return dateFormat.parse(dateString);
    }
}

