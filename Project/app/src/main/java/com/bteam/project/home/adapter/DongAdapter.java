package com.bteam.project.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.R;
import com.bteam.project.home.model.City;
import com.bteam.project.util.MyMotionToast;

import java.util.ArrayList;

public class DongAdapter extends RecyclerView.Adapter<DongAdapter.DongViewHolder> {

    private ArrayList<City> cities;

    public DongAdapter(ArrayList<City> cities) {
        this.cities = cities;
    }

    @NonNull
    @Override
    public DongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DongViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dong, parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DongViewHolder holder, int position) {
        City city = cities.get(position);
        String address = city.getSi() + " " + city.getGu() + " " + city.getDong();
        holder.dong_result.setText(address);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    class DongViewHolder extends RecyclerView.ViewHolder {

        TextView dong_result;
        Button dong_pick;

        public DongViewHolder(@NonNull final View itemView) {
            super(itemView);
            dong_result = itemView.findViewById(R.id.dong_result);
            dong_pick = itemView.findViewById(R.id.dong_pick);

            dong_pick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (listener != null) {
                            listener.onItemClick(view, position);
                        }
                    }
                }
            });
        }

    }

    public City get(int position) {
        return cities.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener listener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
