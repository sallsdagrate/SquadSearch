package com.drp52.data.database;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FixtureDate extends Date {
    public FixtureDate(int year,
                       int month,
                       int date,
                       int hrs,
                       int mi){
        super(year, month, date, hrs, mi);
    }

    public FixtureDate(String s) {
        super(0,
                Integer.parseInt(s.substring(3,5)),
                Integer.parseInt(s.substring(0,2)),
                Integer.parseInt(s.substring(6,8)),
                Integer.parseInt(s.substring(9,11)));
    }

    @NonNull
    @Override
    public String toString() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm");
        return dateFormat.format(this);
    }

    public String toDatabaseString() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm");
        return dateFormat.format(this);
    }


    public static String databaseToString(String database) {
        StringBuilder sb = new StringBuilder();
        List<Integer> remap = List.of(3, 4, 2, 0, 1, 5, 9, 10, 8, 6, 7);
        for (Integer i: remap) {
            sb.append(database.charAt(i));
        }
        return sb.toString();
    }
}
