package com.example.weather.model;

import java.util.List;

public class City {
    private  int id;
    private String cityName;
    private String countryName;
    private Location location;
    private long sunrise;
    private long sunset;
    private List<Weather> weatherList;


    public City() { }

    public City(String cityName, String countryName) {
        this.cityName = cityName;
        this.countryName = countryName;
    }

    public City(String cityName, String countryName, Location location) {
        this.cityName = cityName;
        this.countryName = countryName;
        this.location = location;
    }

    public City(String cityName, String countryName, Location location, long sunrise, long sunset) {
        this.cityName = cityName;
        this.countryName = countryName;
        this.location = location;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public City(String cityName, String countryName, Location location, List<Weather> weatherList) {
        this.cityName = cityName;
        this.countryName = countryName;
        this.location = location;
        this.weatherList = weatherList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

}
