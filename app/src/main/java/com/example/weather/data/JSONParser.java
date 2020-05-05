package com.example.weather.data;


import com.example.weather.model.City;
import com.example.weather.model.Location;
import com.example.weather.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JSONParser {

    public static City getWeather(String data) throws JSONException {

        String dateIndex = "";

        JSONObject jsonObject = new JSONObject(data);

        JSONObject cityObj = jsonObject.getJSONObject("city");
        String cityName = cityObj.getString("name");
        String countryName = cityObj.getString("country");
        long sunrise = cityObj.getLong("sunrise");
        long sunset = cityObj.getLong("sunset");

        JSONObject coordObj = cityObj.getJSONObject("coord");
        double lat = coordObj.getDouble("lat");
        double lon = coordObj.getDouble("lon");

        Location location = new Location(lat, lon);

        City city = new City(cityName, countryName, location, sunrise, sunset);

        //start getting weather for 5 days.
        List<Weather> weatherList = new ArrayList<>();

        JSONArray jsonArray = jsonObject.getJSONArray("list");
        for(int i = 0; i< jsonArray.length(); i++){
            Weather weather = new Weather();

            JSONObject obj = jsonArray.getJSONObject(i);
            String time = obj.getString("dt_txt");
            String clock = time.substring(11);
            JSONObject mainObj = obj.getJSONObject("main");

            String date = time.substring(0, 10);
            if( i == 0 ){
                if(clock.equals("00:00:00") || clock.equals("03:00:00") || clock.equals("06:00:00") || clock.equals("09:00:00")){
                    continue;
                } else {
                    double temp_h = mainObj.getDouble("temp_max");
                    int highTemp = (int) Math.round(temp_h);
                    double temp_l = mainObj.getDouble("temp_min");
                    int lowTemp = (int) Math.round(temp_l);
                    weather.setHighTemp(highTemp);
                    weather.setLowTemp(lowTemp);
                    dateIndex = date;
                }

            } else {
                //get the high-temp
                if(clock.equals("12:00:00")){
                    double temp_h = mainObj.getDouble("temp_max");
                    int highTemp = (int) Math.round(temp_h);
                    weather.setHighTemp(highTemp);
                }
                //get the low-temp
                else if(clock.equals("21:00:00")){
                    double temp_l = mainObj.getDouble("temp_min");
                    int lowTemp = (int) Math.round(temp_l);
                    weather.setLowTemp(lowTemp);
                }
                else if(dateIndex.equals(date)){
                    continue;
                }
                else {
                    dateIndex = date;
                    continue;
                }
            }

            double temp_feels_like = mainObj.getDouble("feels_like");
            int feels_like = (int) Math.round(temp_feels_like);

            int pressure = mainObj.getInt("pressure");

            int humidity = mainObj.getInt("humidity");

            JSONArray weatherArray = obj.getJSONArray("weather");
            JSONObject weatherObj = weatherArray.getJSONObject(0);

            int id = weatherObj.getInt("id");

            String description = weatherObj.getString("description");

            JSONObject windObj = obj.getJSONObject("wind");
            double wind_speed = windObj.getDouble("speed");

            weather.setDescription(description);
            weather.setFeels_like(feels_like);
            weather.setWeatherId(id);
            weather.setWind(wind_speed);
            weather.setPressure(pressure);
            weather.setHumidity(humidity);
            weather.setTime(time);

            weather.setCity(cityName);
            weatherList.add(weather);

        }

        List<Weather> list = new ArrayList<>();

        if(weatherList.get(0).getTime().substring(11).equals("21:00:00") ){

            Weather w = new Weather();
            w.setHighTemp(weatherList.get(0).getHighTemp());
            w.setLowTemp(weatherList.get(0).getLowTemp());
            w.setDescription(weatherList.get(0).getDescription());
            w.setFeels_like(weatherList.get(0).getFeels_like());
            w.setWeatherId(weatherList.get(0).getWeatherId());
            w.setWind(weatherList.get(0).getWind());
            w.setPressure(weatherList.get(0).getPressure());
            w.setHumidity(weatherList.get(0).getHumidity());
            w.setTime(weatherList.get(0).getTime());
            list.add(w);

            for(int i = 1; i < weatherList.size()-1; i+=2){

                Weather weather = new Weather();
                int highTemp = weatherList.get(i).getHighTemp();
                weather.setHighTemp(highTemp);
                int lowTemp = weatherList.get(i+1).getLowTemp();
                weather.setLowTemp(lowTemp);
                weather.setDescription(weatherList.get(i).getDescription());
                weather.setFeels_like(weatherList.get(i).getFeels_like());
                weather.setWeatherId(weatherList.get(i).getWeatherId());
                weather.setWind(weatherList.get(i).getWind());
                weather.setPressure(weatherList.get(i).getPressure());
                weather.setHumidity(weatherList.get(i).getHumidity());
                weather.setTime(weatherList.get(i).getTime());

                list.add(weather);
            }
        }
        else{
            for(int i = 0; i < weatherList.size()-1; i+=2){

                Weather weather = new Weather();
                int highTemp = weatherList.get(i).getHighTemp();
                weather.setHighTemp(highTemp);
                int lowTemp = weatherList.get(i+1).getLowTemp();
                weather.setLowTemp(lowTemp);
                weather.setDescription(weatherList.get(i).getDescription());
                weather.setFeels_like(weatherList.get(i).getFeels_like());
                weather.setWeatherId(weatherList.get(i).getWeatherId());
                weather.setWind(weatherList.get(i).getWind());
                weather.setPressure(weatherList.get(i).getPressure());
                weather.setHumidity(weatherList.get(i).getHumidity());
                weather.setTime(weatherList.get(i).getTime());

                list.add(weather);
            }
        }

        city.setWeatherList(list);
        return city;
    }
}



