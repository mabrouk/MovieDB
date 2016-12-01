package com.mabrouk.moviedb.common.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 11/7/2016.
 */

public class DateUtils {
    public static String formatDateString(String dateString) {
        if(dateString.isEmpty())
            return "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date tmp = null;
        try {
            tmp = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateFormat = new SimpleDateFormat("dd MMM yyyy");
        return dateFormat.format(tmp);
    }
}
