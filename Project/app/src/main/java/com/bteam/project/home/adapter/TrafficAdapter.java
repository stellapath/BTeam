package com.bteam.project.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.R;
import com.bteam.project.home.model.Traffic;

import java.util.List;

public class TrafficAdapter extends RecyclerView.Adapter<TrafficAdapter.TrafficViewHolder> {

    private List<Traffic> list;

    public TrafficAdapter(List<Traffic> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public TrafficViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrafficViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_traffic, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TrafficViewHolder holder, int position) {
        holder.setTraffic(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class TrafficViewHolder extends RecyclerView.ViewHolder {

        TextView num, accident, street, detail, time;

        public TrafficViewHolder(@NonNull View itemView) {
            super(itemView);
            num = itemView.findViewById(R.id.traffic_num);
            accident = itemView.findViewById(R.id.traffic_accident);
            street = itemView.findViewById(R.id.traffic_street);
            detail = itemView.findViewById(R.id.traffic_detail);
            time = itemView.findViewById(R.id.traffic_time);
        }

        public void setTraffic(Traffic traffic) {
            num.setText(traffic.getNum());
            accident.setText(traffic.getTypeOfAccident());
            street.setText(traffic.getStreetName());
            detail.setText(traffic.getDetail());
            time.setText(traffic.getTime());
        }
    }
}
