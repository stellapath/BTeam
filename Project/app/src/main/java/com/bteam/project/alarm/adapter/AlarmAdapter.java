package com.bteam.project.alarm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.R;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    List<String> list;

    public AlarmAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlarmViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        holder.alarm_text.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AlarmViewHolder extends RecyclerView.ViewHolder {

        TextView alarm_text;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            alarm_text = itemView.findViewById(R.id.alarm_item_text);
        }
    }
}
