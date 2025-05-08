package com.acroninspector.app.common.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtil {

    private DateUtil() {
    }

    public static String convertLongDateToString(long date) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss",
                Locale.getDefault()).format(date);
    }

    public static String reversedConvertLongDateToString(long date) {
        return new SimpleDateFormat("yy.MM.dd HH:mm:ss",
                Locale.getDefault()).format(date);
    }
}
