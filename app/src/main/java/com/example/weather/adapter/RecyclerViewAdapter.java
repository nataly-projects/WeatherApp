package com.example.weather.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.ExtraDetailActivity;
import com.example.weather.R;
import com.example.weather.WeatherActivity;
import com.example.weather.model.City;
import com.example.weather.model.Weather;
import com.example.weather.util.Util;

import com.google.gson.Gson;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter{

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    private String activity;
    private Context context;
    private List<City> cityList; //from the main activity we get the list of cities from the DB.
    private City cityObj;

    public RecyclerViewAdapter(Context context, List<City> cityList, String activity, City city) {
        this.context = context;
        this.cityList = cityList;
        this.activity = activity;
        this.cityObj = city;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutId = 2;
        switch (activity) {
            case "main":
                View city_view = LayoutInflater.from(context).inflate(R.layout.list_cities_row, viewGroup, false);
                return new cityHolder(city_view, context);

            case "weather":
                switch (viewType){
                    case VIEW_TYPE_TODAY:
                        layoutId = R.layout.list_item_forecast_today;
                        break;

                    case VIEW_TYPE_FUTURE_DAY:
                        layoutId = R.layout.list_weather_row;
                        break;
                }
                View weather_view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
                return new weatherHolder(weather_view, context);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch (activity){
            case "main":
                City city = cityList.get(position);
                Weather weather = city.getWeatherList().get(0);
                int icon_id = weather.getWeatherId();

                ((cityHolder)viewHolder).cityTextView.setText(city.getCityName());
                ((cityHolder)viewHolder).countryTextView.setText(city.getCountryName());
                ((cityHolder)viewHolder).tempTextView.setText(weather.getHighTemp() + "\u2103");
                ((cityHolder)viewHolder).weatherImageView.setImageResource(Util.getResourceIdForWeatherCondition(icon_id));
                break;

            case "weather":

                if(position == 0){
                    ((weatherHolder)viewHolder).cityTextView.setText(cityObj.getCityName());
                    ((weatherHolder)viewHolder).mapLocationButton.setOnClickListener((View.OnClickListener) viewHolder);
                    ((weatherHolder)viewHolder).websiteLink.setOnClickListener((View.OnClickListener) viewHolder);

                    long sunrise = cityObj.getSunrise();
                    ((weatherHolder)viewHolder).sunrise.setText(Util.convertTime(sunrise));

                    long sunset = cityObj.getSunset();
                    ((weatherHolder)viewHolder).sunset.setText(Util.convertTime(sunset));
                }
                Weather weath = cityObj.getWeatherList().get(position);

                String time = weath.getTime();
                ((weatherHolder)viewHolder).dateTextView.setText(Util.convertDate(time));
                ((weatherHolder)viewHolder).tempHighTextView.setText(weath.getHighTemp() + "\u00b0");
                ((weatherHolder)viewHolder).tempLowTextView.setText(weath.getLowTemp() + "\u00b0");
                ((weatherHolder)viewHolder).weatherDescription.setText(weath.getDescription());
                ((weatherHolder)viewHolder).weatherIcon.setImageResource(Util.getResourceIdForWeatherCondition(weath.getWeatherId()));
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TODAY;
        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }


    @Override
    public int getItemCount() {
        switch (activity){
            case "main":
                return cityList.size();

            case "weather":
                return cityObj.getWeatherList().size();
        }
        return 0;
    }

    public class cityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView cityTextView;
        private TextView countryTextView;
        private TextView tempTextView;
        private ImageView weatherImageView;
        private CardView cardView;

        public cityHolder(@NonNull View view, Context ctx) {
            super(view);
            context = ctx;

            cityTextView = view.findViewById(R.id.city);
            countryTextView = view.findViewById(R.id.country);
            tempTextView = view.findViewById(R.id.temp);
            weatherImageView = view.findViewById(R.id.weather_icon);
            cardView = view.findViewById(R.id.cardView);

            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            City city = cityList.get(position);

            switch (v.getId()){

                //start activity - go the city week-weather activity.
                case R.id.cardView:
                    Intent intent = new Intent(context, WeatherActivity.class);
                    Gson gson = new Gson();
                    String cityGson = gson.toJson(city);

                    intent.putExtra("city", cityGson);
                    context.startActivity(intent);
                    break;
            }
        }
    }

    public class weatherHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView weatherIcon;
        private TextView dateTextView;
        private TextView weatherDescription;
        private TextView tempHighTextView;
        private TextView tempLowTextView;
        private ConstraintLayout weatherLayout;

        private TextView sunset;
        private TextView sunrise;
        private TextView cityTextView;
        private ImageView mapLocationButton;
        private ImageView websiteLink;


        public weatherHolder(@NonNull View view, Context ctx) {
            super(view);
            context = ctx;

            weatherIcon = view.findViewById(R.id.weather_icon);
            dateTextView = view.findViewById(R.id.date);
            weatherDescription = view.findViewById(R.id.weather_description);
            tempHighTextView = view.findViewById(R.id.high_temperature);
            tempLowTextView = view.findViewById(R.id.low_temperature);
            weatherLayout = view.findViewById(R.id.weather);

            weatherLayout.setOnClickListener(this);

            sunset = view.findViewById(R.id.sunset);
            sunrise = view.findViewById(R.id.sunrise);
            cityTextView = view.findViewById(R.id.city_name);
            mapLocationButton = view.findViewById(R.id.map_location);
            websiteLink = view.findViewById(R.id.link);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Weather weather = cityObj.getWeatherList().get(position);
            double lat = cityObj.getLocation().getLatitude();
            double lon = cityObj.getLocation().getLongitude();

            switch (v.getId()){
                case R.id.weather:
                    //start activity - move to extra detail activity.
                    Intent intent = new Intent(context, ExtraDetailActivity.class);
                    Gson gson = new Gson();
                    String weatherGson = gson.toJson(weather);
                    String cityGson = gson.toJson(cityObj);

                    intent.putExtra("weather", weatherGson);
                    intent.putExtra("city", cityGson);
                    context.startActivity(intent);
                    break;

                case R.id.map_location:
                    String mapUri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lon);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapUri));
                    context.startActivity(mapIntent);
                    break;

                case R.id.link:
                    String start_url = "https://openweathermap.org/weathermap?basemap=map&cities=true&layer=temperature&";
                    String end_url = "lat="+lat+"&lon="+lon+"zoom=18";

                    String webUri = start_url+end_url;
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUri));
                    context.startActivity(webIntent);
                    break;
            }

        }
    }

}
