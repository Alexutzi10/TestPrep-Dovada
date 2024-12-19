package com.example.testprep_dovada.data;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    @TypeConverter
    public static String fromDate(Date value) {
        if (value == null) {
            return null;
        }
        return formatter.format(value);
    }

    @TypeConverter
    public static Date toDate(String value) {
        try {
            return formatter.parse(value);
        } catch (ParseException ex) {
            return null;
        }
    }
}
