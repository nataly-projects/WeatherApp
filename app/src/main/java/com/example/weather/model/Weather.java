package com.example.weather.model;

public class Weather {

    private String city;
    private String description;
    private int highTemp;
    private int lowTemp;
    private int feels_like;
    private int weatherId;
    private double wind;
    private int pressure;
    private int humidity;
    private String time;


    public Weather() { }

    public Weather(String description, int highTemp, int lowTemp, int feels_like, int weatherId, double wind, int pressure, int humidity, String time) {
        this.description = description;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.feels_like = feels_like;
        this.weatherId = weatherId;
        this.wind = wind;
        this.pressure = pressure;
        this.humidity = humidity;
        this.time = time;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(int highTemp) {
        this.highTemp = highTemp;
    }

    public int getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(int lowTemp) {
        this.lowTemp = lowTemp;
    }

    public int getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(int feels_like) {
        this.feels_like = feels_like;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
