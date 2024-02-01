package com.adititanwar.weatherapp;

import java.util.ArrayList;

public class Weather {
    String wAddress;
    ArrayList<DayInfo> wDayDetails;
    CurrentWeatherInfo wCurrentConditionWeatherInfo;

    public Weather() {
    }

    public void setwAddress(String wAddress) {
        this.wAddress = wAddress;
    }

    public void setwDayDetails(ArrayList<DayInfo> wDayDetails) {
        this.wDayDetails = wDayDetails;
    }

    public void setwCurrentConditionWeatherInfo(CurrentWeatherInfo wCurrentConditionWeatherInfo) {
        this.wCurrentConditionWeatherInfo = wCurrentConditionWeatherInfo;
    }

    public CurrentWeatherInfo getM_oCurrentConditions() {
        return wCurrentConditionWeatherInfo;
    }

    Weather(String address, ArrayList<DayInfo> daysArrayList,
            CurrentWeatherInfo currentWeatherInfoConditions) {
        this.wAddress = address;
        this.wDayDetails = daysArrayList;
        this.wCurrentConditionWeatherInfo = currentWeatherInfoConditions;

    }
}
