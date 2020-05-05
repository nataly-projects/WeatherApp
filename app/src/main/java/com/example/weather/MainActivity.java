package com.example.weather;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.weather.adapter.RecyclerViewAdapter;
import com.example.weather.data.DatabaseHandler;
import com.example.weather.data.WeatherQuery;
import com.example.weather.model.City;
import com.example.weather.util.Util;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final int INTENT_REQUEST_CODE = 1;
    private static final String CHANNEL_NAME = "notifications";
    public static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    private static final int NOTIFICATION_ID = 13;


    private RecyclerView recyclerView;
    private TextView dateTextView;
    private TextView locationTextView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<City> citySavedList;   //get the cities names from the DB.
    private List<City> city_to_recycle; //have all the data for each city
    private DatabaseHandler databaseHandler;
    private ProgressDialog progressDialog;

    LocationManager locationManager;
    private String currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        getLocation();

        dateTextView = findViewById(R.id.date);
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        dateTextView.setText(currentDate);

        locationTextView = findViewById(R.id.current_location);
        locationTextView.setVisibility(View.GONE);

        city_to_recycle = new ArrayList<>();

        databaseHandler = new DatabaseHandler(MainActivity.this);

        citySavedList = databaseHandler.getAllCities();

        if (!citySavedList.isEmpty()) {
            initializeDialog();
        }

        //get update information from the Internet -
        for (int i = 0; i < citySavedList.size(); i++) {
            String cityName = citySavedList.get(i).getCityName();
            JSONWeatherTask task = new JSONWeatherTask();
            task.execute(new String[]{cityName});
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, SearchActivity.class), INTENT_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_CODE && resultCode == RESULT_OK) {
            assert data != null;

            String cityToSearch = data.getStringExtra("city_to_search");
            JSONWeatherTask task = new JSONWeatherTask();
            task.execute(new String[]{cityToSearch});
        }
    }

    private void initializeDialog() {
        progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading...", true);
        progressDialog.show();
    }

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());

    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                City city = city_to_recycle.get(position);
                databaseHandler.deleteCity(city.getCityName());
                city_to_recycle.remove(city);

                updateRecyclerView();
            }
        };
        return simpleCallback;
    }


    private void updateRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //setup the Adapter
        recyclerViewAdapter = new RecyclerViewAdapter(this, city_to_recycle, "main", null);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }


    public class JSONWeatherTask extends AsyncTask<String, Void, City> {


        @Override
        public City doInBackground(String... strings) {
            City city = new City();

            String url = Util.BASE_URL + strings[0] + Util.END_URL;

            try {
                city = WeatherQuery.fetchWeatherData(url);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return city;
        }

        @Override
        public void onPostExecute(City city) {
            super.onPostExecute(city);
            if (city == null) {
                startActivity(new Intent(MainActivity.this, ErrorActivity.class));
            } else {

                if (!findCity(city_to_recycle, city.getCityName())) {
                    city_to_recycle.add(city);
                }

                List<City> list = databaseHandler.getAllCities();

                if (!findCity(list, city.getCityName())) {

                    //not in the list -add to db
                    databaseHandler.addCity(city);
                }

                progressDialog.dismiss();
                updateRecyclerView();
            }
        }

        private boolean findCity(Iterable<City> cities, String name) {
            for (City city : cities) {
                if (name.equals(city.getCityName())) {
                    return true;
                }
            }
            return false;
        }
    }


    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = "lat=" + location.getLatitude() + "&lon=" + location.getLongitude();
        String url = Util.GEO_URL + currentLocation + Util.END_URL;

        CurrentLocationTask task = new CurrentLocationTask();
        task.execute(new String[]{url});

    }

    @Override
    public void onProviderDisabled(String provider) {
        buildAlertMessageNoGps();
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void displayNotification(City city) {

        String title = city.getCityName() + " " + city.getWeatherList().get(0).getHighTemp() + "\u00b0" + " - " +
                city.getWeatherList().get(0).getLowTemp() + "\u00b0";

        String description = city.getWeatherList().get(0).getDescription();

        locationTextView.setVisibility(View.VISIBLE);
        locationTextView.setText(title + " - " + description);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(this, WeatherActivity.class);
        Gson gson = new Gson();
        String cityGson = gson.toJson(city);
        intent.putExtra("city", cityGson);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = taskStackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(description)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Notification notification = notificationBuilder.build();
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
       manager.notify(NOTIFICATION_ID, notification);
    }

    public class CurrentLocationTask extends AsyncTask<String, Void, City> {

        @Override
        public City doInBackground(String... strings) {
            City city = new City();

            try {
                city = WeatherQuery.fetchWeatherData(strings[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return city;
        }

        @Override
        public void onPostExecute(City city) {
            super.onPostExecute(city);

            displayNotification(city);

        }
    }

}




