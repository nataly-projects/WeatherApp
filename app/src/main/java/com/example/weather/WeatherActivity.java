package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.example.weather.adapter.RecyclerViewAdapter;
import com.example.weather.model.City;
import com.google.gson.Gson;


public class WeatherActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        recyclerView = (RecyclerView)findViewById(R.id.weatherRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle bundle = getIntent().getExtras();
        String cityGson = bundle.getString("city");
        Gson gson = new Gson();

        City city = gson.fromJson(cityGson, City.class);

        recyclerViewAdapter = new RecyclerViewAdapter(this, null, "weather", city);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

}
