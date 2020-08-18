package com.hanul.project.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanul.project.R;
import com.hanul.project.ui.home.model.Item;
import com.hanul.project.ui.home.model.Weather;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> items;

    public HomeAdapter(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 0=날씨, 1=지도, 2=알람, 3=댓글
        if (viewType == 0) {
            return new WeatherViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.item_container_weather,
                            parent,
                            false
                    )
            );
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // 0=날씨, 1=지도, 2=알람, 3=댓글
        if (getItemViewType(position) == 0) {
            Weather weather = (Weather) items.get(position).getObject();
            ((WeatherViewHolder) holder).setWeatherData(weather);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    /**
     * 날씨 뷰홀더
     */
    class WeatherViewHolder extends RecyclerView.ViewHolder {

        ImageView image_weather_background, image_weather_icon;
        TextView text_weather_temperature, text_weather_current, text_weather_city;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            image_weather_background = itemView.findViewById(R.id.image_weather_background);
            image_weather_icon = itemView.findViewById(R.id.image_weather_icon);
            text_weather_temperature = itemView.findViewById(R.id.text_weather_temperature);
            text_weather_current = itemView.findViewById(R.id.text_weather_current);
            text_weather_city = itemView.findViewById(R.id.text_weather_city);

            /**
             * 날씨 클릭하면 화면 이동
             */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        void setWeatherData(Weather weather) {
            image_weather_background.setImageResource(weather.getBackground());
            image_weather_icon.setImageResource(weather.getIcon());
            text_weather_temperature.setText(weather.getTemperature());
            text_weather_current.setText(weather.getCurrent());
            text_weather_city.setText(weather.getCity());
        }
    }

}
