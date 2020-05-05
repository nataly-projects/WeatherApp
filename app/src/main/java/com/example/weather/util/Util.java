package com.example.weather.util;

import android.util.Log;
import com.example.weather.MainActivity;
import com.example.weather.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Util {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();;

    public static String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast?q=";
    public static String END_URL = "&appid=90a129f0792e2a1f2e71c1a4ef4d62c1&&units=metric";

    public static String GEO_URL = "https://api.openweathermap.org/data/2.5/forecast?";

    //Database

    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "weather_db";
    public static final String TABLE_CITY = "city_table";

    //Table-city columns
    public static final String KEY_CITY_ID = "id";
    public static final String KEY_CITY_NAME = "city";


    public static final String CREATE_CITY_TABLE = "CREATE TABLE " + Util.TABLE_CITY + "("
            + Util.KEY_CITY_ID + " INTEGER PRIMARY KEY,"
            + Util.KEY_CITY_NAME + " TEXT);";


    public static int getResourceIdForWeatherCondition(int weatherId) {
        /*
         * Based on weather code data for Open Weather Map.
         */
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.art_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.art_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.art_rain;
        } else if (weatherId == 511) {
            return R.drawable.art_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.art_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.art_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.art_fog;
        } else if (weatherId == 761 || weatherId == 771 || weatherId == 781) {
            return R.drawable.art_storm;
        } else if (weatherId == 800) {
            return R.drawable.art_clear;
        } else if (weatherId == 801) {
            return R.drawable.art_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.art_clouds;
        } else if (weatherId >= 900 && weatherId <= 906) {
            return R.drawable.art_storm;
        } else if (weatherId >= 958 && weatherId <= 962) {
            return R.drawable.art_storm;
        } else if (weatherId >= 951 && weatherId <= 957) {
            return R.drawable.art_clear;
        }
        Log.e(LOG_TAG, "Unknown Weather: " + weatherId);
        return R.drawable.art_storm;
    }


    public static String convertDate(String date){
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateObj = null;
        try
        {
            dateObj = form.parse(date);
        }
        catch (ParseException e)
        {

            e.printStackTrace();
        }
        SimpleDateFormat postFormater = new SimpleDateFormat("EEEE, MMMM dd");
        String newDateStr = postFormater.format(dateObj);
        return newDateStr;
    }

    public static String convertTime(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String timeString = sdf.format(new Date(time * 1000));
        return timeString;
    }


}
