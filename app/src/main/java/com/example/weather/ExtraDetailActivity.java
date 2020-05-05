package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weather.model.City;
import com.example.weather.model.Weather;
import com.example.weather.util.Util;
import com.google.gson.Gson;

public class ExtraDetailActivity extends AppCompatActivity {

    private TextView dateText;
    private TextView cityText;
    private TextView weatherDescription;
    private TextView highTemp;
    private TextView lowTemp;
    private TextView tempLike;
    private ImageView weatherIcon;

    private TextView wind;
    private TextView humidity;
    private TextView pressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_detail);

        setupUI();

        Bundle bundle = getIntent().getExtras();
        Gson gson = new Gson();

        String cityString = bundle.getString("city");
        City city = gson.fromJson(cityString, City.class);
        cityText.setText(city.getCityName()+", " + city.getCountryName());

        String weatherGson = bundle.getString("weather");
        Weather weather = gson.fromJson(weatherGson, Weather.class);

        weatherDescription.setText(weather.getDescription());
        highTemp.setText(weather.getHighTemp() + "\u00b0");
        lowTemp.setText(weather.getLowTemp() + "\u00b0");
        tempLike.setText(weather.getFeels_like() + "\u00b0");
        wind.setText(weather.getWind() + " km/h NW");
        humidity.setText(weather.getHumidity() + "%");
        pressure.setText(weather.getPressure() + " hPa");
        weatherIcon.setImageResource(Util.getResourceIdForWeatherCondition(weather.getWeatherId()));

        String time = weather.getTime();
        String dateString = Util.convertDate(time);
        dateText.setText(dateString);
    }

    private void setupUI(){

        dateText = findViewById(R.id.time);
        cityText = findViewById(R.id.city);
        weatherDescription = findViewById(R.id.desc);
        highTemp = findViewById(R.id.high_temp);
        lowTemp = findViewById(R.id.low_temp);
        tempLike = findViewById(R.id.temp_like);
        weatherIcon = findViewById(R.id.icon);
        wind = findViewById(R.id.wind_);
        humidity = findViewById(R.id.humidity_);
        pressure = findViewById(R.id.pressure_);
    }
}
