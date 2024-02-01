package com.adititanwar.weatherapp;
import static com.adititanwar.weatherapp.WeatherDownloader.wObj;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class DayWeatherActivity extends AppCompatActivity {
    private RecyclerView recyclerDaily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        setup();
    }

    private void setup() {
        recyclerDaily = findViewById(R.id.weatherRecyclerView);
        recyclerDaily.setHasFixedSize(true);
        recyclerDaily.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getData();
    }

    private void getData() {
        String location = getIntent().getStringExtra("location");
        boolean fahrenheit = getIntent().getBooleanExtra("FAHRENHEIT",true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(location + "  " + wObj.wDayDetails.size() + " Day");
        WeatherDailyAdapter weatherDailyAdapter = new WeatherDailyAdapter(getApplicationContext(), fahrenheit);
        recyclerDaily.setAdapter(weatherDailyAdapter);
    }


}

