package com.avantgarde.timy.database;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long timeToDate(Date date) {
        if(date == null){
            Log.d("Converters", "timeToDate: NULL");
            return null;
        }
        Log.d("Converters", "timeToDate: "+date.getTime());
        return date.getTime();
    }
}