package com.example.shreyas.hola;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class timeStamp {

    public static String getTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String tm = dtf.format(now);
        return tm;
    }
    public static String getFullTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String tm = dtf.format(now);
        return tm;
    }
}
