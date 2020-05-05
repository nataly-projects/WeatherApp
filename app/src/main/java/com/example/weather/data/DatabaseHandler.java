package com.example.weather.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.weather.model.City;

import com.example.weather.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DB_NAME, null, Util.DB_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Util.CREATE_CITY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_CITY);

        onCreate(db);
    }

    //******************* CRUD operations - for Table city (create, read, update, delete)****************
    public void addCity(City city) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_CITY_NAME, city.getCityName());

        db.insert(Util.TABLE_CITY, null, values);
        db.close();
    }

    public City getCity(String city) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TABLE_CITY,
                new String[]{Util.KEY_CITY_ID,
                        Util.KEY_CITY_NAME},
                Util.KEY_CITY_NAME + "=?",
                new String[]{city}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        City new_city = new City();
        if (cursor != null) {
            new_city.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_CITY_ID))));
            new_city.setCityName(cursor.getString(cursor.getColumnIndex(Util.KEY_CITY_NAME)));
        }
        return new_city;
    }

    public List<City> getAllCities() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<City> cityList = new ArrayList<>();

        Cursor cursor = db.query(Util.TABLE_CITY,
                new String[]{Util.KEY_CITY_ID,
                        Util.KEY_CITY_NAME},
                null, null, null,
                null, Util.KEY_CITY_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_CITY_ID))));
                city.setCityName(cursor.getString(cursor.getColumnIndex(Util.KEY_CITY_NAME)));

                cityList.add(city);
            } while (cursor.moveToNext());
        }
        return cityList;
    }


    public void deleteCity(String city) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Util.TABLE_CITY,
                Util.KEY_CITY_NAME + "=?",
                new String[]{city});
    }

    public int getCityCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String SELECT_ALL = "SELECT * FROM " + Util.TABLE_CITY;

        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        return cursor.getCount();
    }

}
