package com.adititanwar.weatherapp;
import static com.adititanwar.weatherapp.WeatherDownloader.wObj;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private boolean fahren = true;
    String location = "Chicago,IL";
    private ImageView weatherIconIV;
    ConstraintLayout constraintLayout;
    private TextView temperatureTV;
    public TextView currentDateTimeTV;
    private TextView windTV;
    private TextView feelsLikeTV;
    private TextView uvIndexTV;
    private TextView weatherDescriptionTV;
    private TextView morningTempTV;
    private TextView humidityTV;
    private TextView eveningTempTV;
    private TextView visibilityTV;
    private TextView afternoonTempTV;
    public TextView sunsetTV;
    private TextView nightTempTV;
    public TextView sunriseTV;
    private WeatherHourlyAdapter w_WeatherHourlyAdapter;
    SwipeRefreshLayout w_pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: Start");
        setupWeatherUI();
        getWeatherData();
        w_pullToRefresh = findViewById(R.id.swiper);
        if(!hasNetworkConnection()) {
            //onCreate: No internet connection if block
            currentDateTimeTV.setText(R.string.no_internet_connection);
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
            //onCreate: hiding sunrise and sunset text views
            sunriseTV.setVisibility(View.GONE);
            sunsetTV.setVisibility(View.GONE);
        }
        w_pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                {
                    if(hasNetworkConnection())
                    {
                        //onRefresh: has internet connection and fetching weather data
                        doWeatherDownload();
                    }
                    else{
                        //onRefresh: no internet connection
                        currentDateTimeTV.setText(R.string.no_internet_connection);
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
                        //onRefresh: hiding the constraint view
                        constraintLayout.setVisibility(View.GONE);
                        sunriseTV.setVisibility(View.GONE);
                        sunsetTV.setVisibility(View.GONE);
                        //onRefresh: showing the toast for no internet connection
                        Toast.makeText(getApplicationContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                        w_pullToRefresh.setRefreshing(false);
                    }
                }
                w_pullToRefresh.setRefreshing(false);
            }
        });
        doWeatherDownload();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        //onCreateOptionsMenu: setting units icon
        if (fahren) {
            menu.getItem(0).setIcon(R.drawable.units_f);
        } else if (!fahren) {
            menu.getItem(0).setIcon(R.drawable.units_c);
        }
        return true;
    }
    private void getWeatherData() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        fahren = prefs.getBoolean("FAHRENHEIT", true);
        location = prefs.getString("location", "Chicago, Illinois");
        Objects.requireNonNull(getSupportActionBar()).setTitle("" + location);
    }
    public void saveWeatherData() {
        //saveWeatherData: saving data in shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putBoolean("FAHRENHEIT", fahren);
        editor.putString("location", location);
        editor.apply();
    }
    private void setupWeatherUI() {
        //"setupWeatherUI: setting all the views
        constraintLayout = (ConstraintLayout) findViewById(R.id.wholeConstraintLayout);
        weatherIconIV = findViewById(R.id.weatherIconImageView);
        weatherDescriptionTV = findViewById(R.id.weatherDescriptionTextView);
        currentDateTimeTV = findViewById(R.id.currentDateTimeTextView);
        windTV = findViewById(R.id.windsTextView);
        temperatureTV = findViewById(R.id.currentTemperatureTextView);
        uvIndexTV = findViewById(R.id.uvIndexTextView);
        feelsLikeTV = findViewById(R.id.feelsLikeTextView);
        afternoonTempTV = findViewById(R.id.afternoonTempTextView);
        humidityTV = findViewById(R.id.humidityTextView);
        eveningTempTV = findViewById(R.id.eveningTempTextView);
        visibilityTV = findViewById(R.id.visibilityTextView);
        morningTempTV = findViewById(R.id.morningTempTextView);
        sunsetTV = findViewById(R.id.sunsetTextView);
        nightTempTV = findViewById(R.id.nightTempTextView);
        sunriseTV = findViewById(R.id.sunriseTextView);
        RecyclerView weatherRecyclerView = findViewById(R.id.hourlyRecyclerView);
        w_WeatherHourlyAdapter = new WeatherHourlyAdapter(MainActivity.this, true);
        weatherRecyclerView.setHasFixedSize(true);
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        weatherRecyclerView.setAdapter(w_WeatherHourlyAdapter);
    }
    private int getWeatherIcon(String iconName) {
        String icon = iconName;
        icon = icon.replace("-", "_"); // Replace all dashes with underscores
        int iconID =
                MainActivity.this.getResources().getIdentifier(icon, "drawable", MainActivity.this.getPackageName());
        if (iconID == 0) {
            //parseCurrentRecord: CANNOT FIND ICON
            return 0;
        }
        return iconID;
    }
    private void doWeatherDownload() {
        //doWeatherDownload: downloading weather info using volley
        WeatherDownloader.downloadWeather(this,location , fahren);
    }

    private String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }

    public boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(hasNetworkConnection()) {
            if (item.getItemId() == R.id.dailyForecastItem) {
                //onOptionsItemSelected: creating intent for DayWeatherActivity
                Intent intent = new Intent(getApplicationContext(), DayWeatherActivity.class);
                intent.putExtra("location", location);
                intent.putExtra("FAHRENHEIT", fahren);
                //onOptionsItemSelected: starting the activity on click of dailyForecastItem
                startActivity(intent);
                return true;
            }
            else if (item.getItemId() == R.id.changeUnitsItem){
                //onOptionsItemSelected: changeUnitsItem is clicked
                if (!fahren) {
                    fahren = true;
                    item.setIcon(R.drawable.units_f);
                }
                else if (fahren) {
                    fahren = false;
                    item.setIcon(R.drawable.units_c);
                }
                //onOptionsItemSelected: loading weather based on the selected units
                doWeatherDownload();
                //onOptionsItemSelected: saving data in the shared preferences
                saveWeatherData();
                return true;
            }
            else if (item.getItemId() == R.id.locationItem)
            {
                //onOptionsItemSelected: location item is selected
                EditText edittext = new EditText(MainActivity.this);
                //onOptionsItemSelected: building the alert dialog for location update
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("For US locations, enter as 'City' or 'City, State'\n\nFor international locations enter as 'City, Country'");
                alert.setTitle("Enter a Location");
                alert.setView(edittext);
                //onOptionsItemSelected: setting positive and negative buttons of the alert
                alert.setNegativeButton("CANCEL", (dialog, whichButton) -> dialog.dismiss());
                alert.setPositiveButton("OK", (dialog, whichButton) -> {
                    //onOptionsItemSelected: OK Button clicked
                    location = edittext.getText().toString();
                    //onOptionsItemSelected: fetching new location weather info
                    doWeatherDownload();
                    Objects.requireNonNull(getSupportActionBar()).setTitle("" + location);
                    //onOptionsItemSelected: saving data in shared preferences
                    saveWeatherData();
                });
                //onOptionsItemSelected: showing the alert
                alert.show();
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateData(Weather weather) {
        //updateData: Received the response through volley
        String unitSign = "F";
        if (!fahren) unitSign = "C";

        if (wObj == null)
            return;

        if (weather == null) {
            Toast.makeText(this, "Please Enter a Valid City Name", Toast.LENGTH_SHORT).show();
            return;
        }
        //updateData: formatting dates
        SimpleDateFormat formatter = new SimpleDateFormat("EE MMM d hh:mm a, yyyy", Locale.ENGLISH);
        String direction = getDirection(wObj.wCurrentConditionWeatherInfo.windDirection);
        SimpleDateFormat timeOnly = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String sunrise = timeOnly.format(new Date(wObj.wCurrentConditionWeatherInfo.sunriseEpoch * 1000L));
        String dateString = formatter.format(new Date(wObj.wCurrentConditionWeatherInfo.dateTimeEpoch * 1000L));
        String sunset = timeOnly.format(new Date(wObj.wCurrentConditionWeatherInfo.sunsetEpoch * 1000L));

        int iconID = getWeatherIcon(wObj.wCurrentConditionWeatherInfo.icon);
        if (iconID != 0)
            weatherIconIV.setImageResource(iconID);

        if(hasNetworkConnection())
            currentDateTimeTV.setText(MessageFormat.format("{0}", dateString));
        else {
            currentDateTimeTV.setText(R.string.no_internet_connection);
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        }
        weatherDescriptionTV.setText(MessageFormat.format("{0} ({1}% clouds)", wObj.wCurrentConditionWeatherInfo.conditions, (int) Math.round(wObj.wCurrentConditionWeatherInfo.cloudCover)));
        temperatureTV.setText(String.format("%.0f° " + (fahren ? "F" : "C"), wObj.wCurrentConditionWeatherInfo.temp));
        humidityTV.setText(MessageFormat.format("Humidity: {0}%", (int) Math.round(wObj.wCurrentConditionWeatherInfo.humidity)));
        feelsLikeTV.setText(MessageFormat.format("Feels Like {0}°{1}", (int) Math.round(wObj.wCurrentConditionWeatherInfo.feelsLike), unitSign));
        windTV.setText(MessageFormat.format("Winds: {0} at {1} mph gusting to {2} mph", direction, (int) Math.round(wObj.wCurrentConditionWeatherInfo.windSpeed), (int) Math.round(wObj.wCurrentConditionWeatherInfo.windGust)));
        morningTempTV.setText(MessageFormat.format("{0}°{1}", (int) Math.round(wObj.wDayDetails.get(0).hourInfoList.get(8).temp), unitSign));
        uvIndexTV.setText(MessageFormat.format("UV Index: {0}", (int) Math.round(wObj.wCurrentConditionWeatherInfo.uvIndex)));
        visibilityTV.setText(MessageFormat.format("Visibility: {0} mi", wObj.wCurrentConditionWeatherInfo.visibility));
        afternoonTempTV.setText(MessageFormat.format("{0}°{1}", (int) Math.round(wObj.wDayDetails.get(0).hourInfoList.get(13).temp), unitSign));
        nightTempTV.setText(MessageFormat.format("{0}°{1}", (int) Math.round(wObj.wDayDetails.get(0).hourInfoList.get(21).temp), unitSign));
        sunriseTV.setText(MessageFormat.format("Sunrise: {0}", sunrise));
        eveningTempTV.setText(MessageFormat.format("{0}°{1}", (int) Math.round(wObj.wDayDetails.get(0).hourInfoList.get(17).temp), unitSign));
        sunsetTV.setText(MessageFormat.format("Sunset: {0}", sunset));
        w_WeatherHourlyAdapter.updateData(fahren);
    }
}