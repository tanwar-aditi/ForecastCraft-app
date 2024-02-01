package com.adititanwar.weatherapp;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class WeatherDownloader {

    private static final String TAG = "WeatherDownloader";

    private static MainActivity mainAct;
    private static RequestQueue que;
    public static Weather wObj;

    private static final String weatherURL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    private static final String yourAPIKey = "HDSRGP334NS5SXQECEHJFSBYH";

    public static void downloadWeather(MainActivity mActivity, String lAddress, boolean fahren) {
        mainAct = mActivity;
        if (!mActivity.hasNetworkConnection()) {
            //downloadWeather: hiding info
            mActivity.constraintLayout.setVisibility(View.GONE);
            mActivity.currentDateTimeTV.setText(R.string.no_internet_connection);
            mActivity.sunsetTV.setVisibility(View.GONE);
            mActivity.sunriseTV.setVisibility(View.GONE);
            //downloadWeather: showing toast of no internet connection
            Toast.makeText(mActivity.getApplicationContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            mActivity.w_pullToRefresh.setRefreshing(false);
            return;
        } else {
            //downloadWeather: internet connection is present"
            mActivity.constraintLayout.setVisibility(View.VISIBLE);
            mActivity.sunsetTV.setVisibility(View.VISIBLE);
            mActivity.sunriseTV.setVisibility(View.VISIBLE);
            Objects.requireNonNull(mActivity.getSupportActionBar()).setTitle("" + mActivity.location);
        }
        que = Volley.newRequestQueue(mainAct);
        //downloadWeather: building new volley request
        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();
        buildURL.appendPath(lAddress);
        buildURL.appendQueryParameter("unitGroup", (fahren ? "us" : "metric"));
        buildURL.appendQueryParameter("lang","en");
        buildURL.appendQueryParameter("key", yourAPIKey);
        String urlToUse = buildURL.build().toString();

        //downloadWeather: error listener defined
        Response.ErrorListener error =
                error1 -> mainAct.updateData(null);
        //downloadWeather: response listener defined
        Response.Listener<JSONObject> listener =
                response -> parseJSON(response.toString());

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);

        // Add the request to the RequestQueue.
        que.add(jsonObjectRequest);
    }


    private static void parseJSON(String s) {
        try {
            JSONObject j_oMain = new JSONObject(s);
            ArrayList<DayInfo> dayInfoDetailList = new ArrayList<>();

            JSONArray jsonArrayDays = j_oMain.getJSONArray("days");

            for (int i=0; i<jsonArrayDays.length(); i++) {
                DayInfo oDayInfo = new DayInfo();
                ArrayList<HourInfo> hourInfoList = new ArrayList<HourInfo>();
                JSONObject jsonObjectDay = jsonArrayDays.getJSONObject(i);
                JSONArray jsonArrayHours = jsonObjectDay.getJSONArray("hours");

                for (int j=0; j<jsonArrayHours.length(); j++) {
                    JSONObject jsonObjectHour = jsonArrayHours.getJSONObject(j);
                    HourInfo oHourInfo = new HourInfo();
                    oHourInfo.datetimeEpoch = jsonObjectHour.getLong("datetimeEpoch");
                    oHourInfo.conditions = jsonObjectHour.getString("conditions");
                    oHourInfo.temp = jsonObjectHour.getDouble("temp");
                    oHourInfo.icon = jsonObjectHour.getString("icon");
                    hourInfoList.add(oHourInfo);
                }
                oDayInfo.hourInfoList = hourInfoList;
                oDayInfo.dateTimeEpoch = jsonObjectDay.getLong("datetimeEpoch");
                oDayInfo.minTemp = jsonObjectDay.getDouble("tempmin");
                oDayInfo.maxTemp = jsonObjectDay.getDouble("tempmax");
                oDayInfo.uvIndex = jsonObjectDay.getDouble("uvindex");
                oDayInfo.precipitationProb = jsonObjectDay.getDouble("precipprob");
                oDayInfo.weatherIcon = jsonObjectDay.getString("icon");
                oDayInfo.tempDescription = jsonObjectDay.getString("description");
                dayInfoDetailList.add(oDayInfo);
            }

            JSONObject jsonObjectCurrentConditions = j_oMain.getJSONObject("currentConditions");
            CurrentWeatherInfo currentWeatherInfoConditions = new CurrentWeatherInfo();
            currentWeatherInfoConditions.dateTimeEpoch = jsonObjectCurrentConditions.getLong("datetimeEpoch");
            currentWeatherInfoConditions.sunriseEpoch = jsonObjectCurrentConditions.getLong("sunriseEpoch");
            currentWeatherInfoConditions.temp = jsonObjectCurrentConditions.getDouble("temp");
            currentWeatherInfoConditions.sunsetEpoch = jsonObjectCurrentConditions.getLong("sunsetEpoch");
            currentWeatherInfoConditions.humidity = jsonObjectCurrentConditions.getDouble("humidity");
            currentWeatherInfoConditions.feelsLike = jsonObjectCurrentConditions.getDouble("feelslike");
            currentWeatherInfoConditions.windSpeed = jsonObjectCurrentConditions.getDouble("windspeed");
            if (!jsonObjectCurrentConditions.isNull("windgust"))
                currentWeatherInfoConditions.windGust = jsonObjectCurrentConditions.getDouble("windgust");
            else
                currentWeatherInfoConditions.windGust = 0.0;currentWeatherInfoConditions.windDirection = jsonObjectCurrentConditions.getDouble("winddir");
            currentWeatherInfoConditions.visibility = jsonObjectCurrentConditions.getDouble("visibility");
            currentWeatherInfoConditions.uvIndex = jsonObjectCurrentConditions.getDouble("uvindex");
            currentWeatherInfoConditions.cloudCover = jsonObjectCurrentConditions.getDouble("cloudcover");
            currentWeatherInfoConditions.icon = jsonObjectCurrentConditions.getString("icon");
            currentWeatherInfoConditions.conditions = jsonObjectCurrentConditions.getString("conditions");

            Weather mWeather = new Weather();
            mWeather.setwDayDetails(dayInfoDetailList);
            mWeather.setwAddress(j_oMain.getString("address"));
            mWeather.setwCurrentConditionWeatherInfo(currentWeatherInfoConditions);

            wObj = mWeather;
            mainAct.updateData(wObj);
            mainAct.w_pullToRefresh.setRefreshing(false);
            mainAct.saveWeatherData();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
