package com.example.user.banhangonline.untils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeNowUtils {

    public static String getTimeNow() {
        DateFormat df = new SimpleDateFormat("dd 'thg' MM, yyyy 'lúc' HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }
}
