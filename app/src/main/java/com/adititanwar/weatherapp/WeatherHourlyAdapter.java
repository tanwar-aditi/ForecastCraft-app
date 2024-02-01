package com.adititanwar.weatherapp;
import static com.adititanwar.weatherapp.WeatherDownloader.wObj;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WeatherHourlyAdapter extends RecyclerView.Adapter<WeatherHourlyViewHolder> {
    private final Context wContext;

    private boolean wFahren;
    ArrayList<HourInfo> h;
    private static final String TAG = "WeatherHourlyAdapter";
    public WeatherHourlyAdapter(Context context, boolean fahren) {
        this.wContext = context;
        this.wFahren = fahren;
    }

    @NonNull
    @Override
    public WeatherHourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_weather_list_item, parent, false);
        return new WeatherHourlyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WeatherHourlyViewHolder holder, int position) {
        h = new ArrayList<>();
        long t = System.currentTimeMillis();
        if(!wObj.wDayDetails.get(0).hourInfoList.isEmpty()) {
            for(int j=0;j<4 && h.size()<=48;j++) {
                for (int i = 0; i < wObj.wDayDetails.get(j).hourInfoList.size() && h.size()<=48; i++){
                    HourInfo hr= wObj.wDayDetails.get(j).hourInfoList.get(i);
                    if (j == 0) {
                        SimpleDateFormat onlyTime = new SimpleDateFormat("h:mm a", Locale.getDefault());
                        String time = onlyTime.format(new Date(hr.datetimeEpoch * 1000L));
                        if(t < (hr.datetimeEpoch * 1000L)) {
                            h.add(wObj.wDayDetails.get(j).hourInfoList.get(i));
                        }
                    }else {
                        h.add(wObj.wDayDetails.get(j).hourInfoList.get(i));
                    }
                }
            }
        }

        HourInfo oHourInfo = h.get(position);

        SimpleDateFormat formatter = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        String dayString = formatter.format(new Date(oHourInfo.datetimeEpoch * 1000L));
        String currentDay = formatter.format(new Date());
        holder.dayTextTV.setText(R.string.today);
        if(!currentDay.equalsIgnoreCase(dayString))
            holder.dayTextTV.setText(dayString);
        else {
            holder.dayTextTV.setText(R.string.today);
        }
        SimpleDateFormat onlyTime = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String time = onlyTime.format(new Date(oHourInfo.datetimeEpoch * 1000L));
        int iconID = getWeatherIcon(wObj.wCurrentConditionWeatherInfo.icon);;
        if(!h.isEmpty())
            iconID = getWeatherIcon(h.get(position).icon);
        if (iconID != 0) holder.hourlyIV.setImageResource(iconID);

        String unitSign = "F";
        if (!wFahren)
            unitSign = "C";

        holder.hourlyTempTV.setText("" + (int) Math.round(oHourInfo.temp) + "°" + unitSign);
        holder.timeTV.setText("" + time);
        holder.hourlyTempDescriptionTV.setText(oHourInfo.conditions);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(boolean fahrenheit) {
        this.wFahren = fahrenheit;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (wObj == null) return 0;
        return wObj.wDayDetails.get(0).hourInfoList.size()+ wObj.wDayDetails.get(1).hourInfoList.size();
    }

    private int getWeatherIcon(String iconName) {
        String icon = iconName;
        icon = icon.replace("-", "_"); // Replace all dashes with underscores
        int iconID = wContext.getResources().getIdentifier(icon, "drawable", wContext.getPackageName());
        if (iconID == 0) {
            String TAG = "HourlyWeatherAdapter";
            return 0;
        }

        return iconID;
    }

}
