package com.bteam.project.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.R;
import com.bteam.project.home.model.Weather;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    ArrayList<Weather> list;

    public WeatherAdapter(ArrayList<Weather> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Weather weather = list.get(position);
        holder.setWeather(weather);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder {

        ImageView background, icon;
        TextView hour, temperature, current;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.item_weather_background);
            icon = itemView.findViewById(R.id.item_weather_icon);
            hour = itemView.findViewById(R.id.item_weather_hour);
            temperature = itemView.findViewById(R.id.item_weather_temperature);
            current = itemView.findViewById(R.id.item_weather_current);
        }

        void setWeather(Weather weather) {
            background.setImageResource(weather.getBackground());
            icon.setImageResource(weather.getIcon());
            hour.setText(weather.getHour());
            temperature.setText(weather.getTemperature());
            current.setText(weather.getCurrent());
        }
    }

}
