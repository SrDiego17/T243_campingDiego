package es.unizar.eina.T243_camping.database;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {

    // Convierte Date a long (milisegundos desde 1970)
    @TypeConverter
    public static Long fromDate(Date date) {
        return (date == null) ? null : date.getTime();
    }

    // Convierte long (milisegundos desde 1970) a Date
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return (timestamp == null) ? null : new Date(timestamp);
    }
}
