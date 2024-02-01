package com.adititanwar.weatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherHourlyViewHolder extends RecyclerView.ViewHolder {
    TextView timeTV;
    ImageView hourlyIV;
    TextView hourlyTempTV;
    TextView dayTextTV;
    TextView hourlyTempDescriptionTV;

    public WeatherHourlyViewHolder(@NonNull View itemView) {
        super(itemView);
        dayTextTV = itemView.findViewById(R.id.dayTextView);
        timeTV = itemView.findViewById(R.id.timeTextView);
        hourlyIV = itemView.findViewById(R.id.hourlyImageView);
        hourlyTempTV = itemView.findViewById(R.id.hourlyTempTextView);
        hourlyTempDescriptionTV = itemView.findViewById(R.id.hourlyTempDescriptionTextView);
    }
}
