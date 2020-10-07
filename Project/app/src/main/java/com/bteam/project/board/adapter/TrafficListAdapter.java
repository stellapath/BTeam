package com.bteam.project.board.adapter;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.R;
import com.bteam.project.board.model.TrafficVO;

import java.util.List;

public class TrafficListAdapter extends RecyclerView.Adapter<TrafficListAdapter.TrafficListViewHolder> {

    private List<TrafficVO> list;

    public TrafficListAdapter(List<TrafficVO> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public TrafficListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrafficListViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrafficListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class TrafficListViewHolder extends RecyclerView.ViewHolder {



        public TrafficListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
