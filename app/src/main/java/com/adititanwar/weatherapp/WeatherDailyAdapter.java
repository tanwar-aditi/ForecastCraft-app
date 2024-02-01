package com.adititanwar.weatherapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.adititanwar.weatherapp.WeatherDownloader.wObj;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WeatherDailyAdapter extends RecyclerView.Adapter<WeatherDailyViewHolder>{
    private final Context wContext;
    private boolean wFahren;

    public WeatherDailyAdapter(Context context, boolean fahren) {
        this.wContext = context;
        this.wFahren = fahren;
    }

    @NonNull
    @Override
    public WeatherDailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_weather_list_item, parent, false);
        return new WeatherDailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherDailyViewHolder holder, int position) {
        String unitSign = "F";
        if (!wFahren)
            unitSign = "C"
                    ;
        DayInfo wDayInfo;
        ArrayList<DayInfo> d = new ArrayList<>();
        for(int i = 0; i< wObj.wDayDetails.size(); i++)
            d.add(wObj.wDayDetails.get(i));
        wDayInfo = d.get(position);

        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MM/dd", Locale.ENGLISH);
        String dateString = formatter.format(new Date(wDayInfo.dateTimeEpoch * 1000L));
        int iconID = getWeatherIcon(wDayInfo.weatherIcon);
        if (iconID != 0) holder.dayIconIV.setImageResource(iconID);
        holder.dayDateTV.setText(dateString);
        holder.dayPrecTV.setText(MessageFormat.format("({0}% precip.)", (int) Math.round(wDayInfo.precipitationProb)));
        holder.dayMinMaxTempTV.setText(MessageFormat.format("{0}°{1}/{2}°{3}", (int) Math.round(wDayInfo.maxTemp), unitSign, (int) Math.round(wDayInfo.minTemp), unitSign));
        holder.dayUVIndexTV.setText(MessageFormat.format("UV Index: {0}", (int) Math.round(wDayInfo.uvIndex)));
        holder.dayDescTV.setText(wDayInfo.tempDescription);
        holder.dayEveningTempTV.setText(MessageFormat.format("{0}°{1}", (int) Math.round(wDayInfo.hourInfoList.get(17).temp), unitSign));
        holder.dayNoonTempTV.setText(MessageFormat.format("{0}°{1}", (int) Math.round(wDayInfo.hourInfoList.get(13).temp), unitSign));
        holder.dayMornTempTV.setText(MessageFormat.format("{0}°{1}", (int) Math.round(wDayInfo.hourInfoList.get(8).temp), unitSign));
        holder.dayNightTempTV.setText(MessageFormat.format("{0}°{1}", (int) Math.round(wDayInfo.hourInfoList.get(21).temp), unitSign));

    }

    @Override
    public int getItemCount() {
        //getItemCount: returning the count of items in the recycler view
        if (wObj == null)
            return 0;
        return wObj.wDayDetails.size();
    }

    private int getWeatherIcon(String iconName) {
        String icon = iconName;
        icon = icon.replace("-", "_"); // Replace all dashes with underscores
        int iconID = wContext.getResources().getIdentifier(icon, "drawable", wContext.getPackageName());
        if (iconID == 0) {
            String TAG = "DailyAdapter";
            return 0;
        }
        return iconID;
    }
}
