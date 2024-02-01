package com.adititanwar.weatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherDailyViewHolder extends RecyclerView.ViewHolder {
    public ImageView dayIconIV;
    public TextView dayMinMaxTempTV;
    public TextView dayDateTV;
    public TextView dayDescTV;
    public TextView dayNightTempTV;
    public TextView dayPrecTV;
    public TextView dayUVIndexTV;
    public TextView dayNoonTempTV;
    public TextView dayMornTempTV;
    public TextView dayEveningTempTV;

    public WeatherDailyViewHolder(@NonNull View itemView) {
        super(itemView);
        dayMinMaxTempTV = itemView.findViewById(R.id.dayMinMaxTempTextView);
        dayDescTV = itemView.findViewById(R.id.dayDescTextView);
        dayIconIV = itemView.findViewById(R.id.dayIconImageView);
        dayUVIndexTV = itemView.findViewById(R.id.dayUVIndexTextView);
        dayDateTV = itemView.findViewById(R.id.dayDateTextView);
        dayMornTempTV = itemView.findViewById(R.id.dayMornTempTextView);
        dayPrecTV = itemView.findViewById(R.id.dayPrecTextView);
        dayNoonTempTV = itemView.findViewById(R.id.dayNoonTempTextView);
        dayEveningTempTV = itemView.findViewById(R.id.dayEveningTempTextView);
        dayNightTempTV = itemView.findViewById(R.id.dayNightTempTextView);
    }
}

